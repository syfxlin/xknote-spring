package me.ixk.xknote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 主要的设置
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:13
 */
@Configuration
@ConfigurationProperties(prefix = "xknote")
@Data
public class XkNoteConfig {

    private String storagePath;
    private String key;
}
