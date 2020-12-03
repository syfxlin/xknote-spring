/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.xknote.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import me.ixk.xknote.config.XkNoteConfig;
import org.springframework.stereotype.Component;

/**
 * 加密工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:02
 */
@Component
public class Crypt {

    private final byte[] key;

    private final Cipher cipher;

    public Crypt(XkNoteConfig config)
        throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key =
            Base64
                .decode(config.getKey())
                .getBytes(StandardCharsets.ISO_8859_1);
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(this.generateIv());
            SecretKeySpec aesKeySpec = new SecretKeySpec(key, "AES");

            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, iv);

            String encrypted = new String(
                cipher.doFinal(value.getBytes(StandardCharsets.ISO_8859_1)),
                StandardCharsets.ISO_8859_1
            );
            String ivEncoded = Base64.encode(iv.getIV());
            String macEncoded = Mac.make(
                "HmacSHA256",
                ivEncoded + encrypted,
                key
            );
            ObjectNode json = Json.createObject();
            json.put("iv", ivEncoded);
            json.put("value", encrypted);
            json.put("mac", macEncoded);
            return Base64.encode(json.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            ObjectNode payload = Json.parseObject(Base64.decode(encrypted));
            if (!this.vaild(Objects.requireNonNull(payload))) {
                return null;
            }
            IvParameterSpec iv = new IvParameterSpec(
                Base64
                    .decode(payload.get("iv").asText())
                    .getBytes(StandardCharsets.ISO_8859_1)
            );
            SecretKeySpec aesKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKeySpec, iv);
            return new String(
                cipher.doFinal(
                    payload
                        .get("value")
                        .asText()
                        .getBytes(StandardCharsets.ISO_8859_1)
                ),
                StandardCharsets.ISO_8859_1
            );
        } catch (Exception e) {
            return null;
        }
    }

    public boolean vaild(ObjectNode payload) {
        if (
            !payload.has("iv") || !payload.has("mac") || !payload.has("value")
        ) {
            return false;
        }
        if (Base64.decode(payload.get("iv").asText()).length() != 16) {
            return false;
        }
        return payload
            .get("mac")
            .asText()
            .equals(
                Mac.make(
                    "HmacSHA256",
                    payload.get("iv").asText() + payload.get("value").asText(),
                    key
                )
            );
    }

    public byte[] generateKey() {
        return this.generateRandom(256);
    }

    public byte[] generateIv() {
        return this.generateRandom(128);
    }

    public byte[] generateRandom(int length) {
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance("AES");
            generator.init(length);
            SecretKey key = generator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
