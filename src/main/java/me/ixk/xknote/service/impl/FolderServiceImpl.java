package me.ixk.xknote.service.impl;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.util.regex.Pattern;
import me.ixk.xknote.utils.JSON;
import me.ixk.xknote.utils.Storage;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl {

    public JsonNode get(String dir) {
        return this.get(dir, false);
    }

    public JsonNode get(String dir, boolean checkGit) {
        return this.get(dir, checkGit, ReadMode.ALL);
    }

    public JsonNode get(String dir, boolean checkGit, ReadMode mode) {
        if (mode == ReadMode.FLAT) {
            return this.getFlat(dir);
        }
        ObjectNode result = JSON.createObject();
        for (File file : Storage.directories(dir)) {
            ObjectNode item = JSON.createObject();
            item.put("type", "folder");
            item.put("path", Storage.processPath(file));
            item.put("name", file.getName());
            item.set("sub", this.get(file.getPath(), false, mode));
            if (checkGit && FileUtil.exist(file.getPath() + "/.git")) {
                item.put("git", true);
            }
            result.set(file.getName(), item);
        }
        if (mode == ReadMode.ALL) {
            String documentExt = "md|txt";
            Pattern documentExtPreg = Pattern.compile(
                "." + documentExt.replace("|", "|.")
            );
            for (File file : Storage.files(dir)) {
                if (documentExtPreg.matcher(file.getName()).find()) {
                    ObjectNode item = JSON.createObject();
                    item.put("type", "note");
                    item.put("path", Storage.processPath(file));
                    item.put("name", file.getName());
                    result.set(file.getName(), item);
                }
            }
        }
        return result;
    }

    public JsonNode getFlat(String dir) {
        return JSON.convertToNode(
            Storage.processPath(Storage.allDirectories(dir))
        );
    }

    public int create(String path) {
        if (Storage.exist(path)) {
            return 409;
        }
        Storage.makeDirectory(path);
        return 200;
    }

    public int delete(String path) {
        Storage.deleteDirectory(path);
        return 200;
    }

    public int move(String oldPath, String newPath) {
        if (!Storage.exist(oldPath)) {
            return 404;
        }
        Storage.move(oldPath, newPath);
        return 200;
    }

    public boolean exist(String path) {
        return Storage.exist(path);
    }
}
