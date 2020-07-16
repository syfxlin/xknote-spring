package me.ixk.xknote.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.ixk.xknote.controller.param.NoteItem;
import me.ixk.xknote.utils.JSON;
import me.ixk.xknote.utils.Storage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class NoteServiceImpl {

    public JsonNode get(String path) {
        if (!Storage.exist(path)) {
            return IntNode.valueOf(404);
        }
        String content = Storage.get(path);
        ObjectNode note = null;
        if (content.contains("===NoteInfo===")) {
            String[] split = content.split("===NoteInfo===[\\r\\n]*");
            note = JSON.parseObject(split[0]);
            note.put("content", split.length == 1 ? "" : split[1]);
        } else {
            note =
                JSON.parseObject(
                    "{\"title\": \"无标题\",\"created_at\": \"null\",\"updated_at\": \"null\",\"author\": \"null\"}"
                );
            note.put("content", content);
        }
        return note;
    }

    public JsonNode getAll(String path) {
        List<File> notes = Storage.allFiles(path);
        return JSON.convertToNode(notes);
    }

    private void set(String path, NoteItem note) {
        ObjectNode node = JSON.convertToObjectNode(note);
        node.remove("content");
        String contents =
            node.toString() + "===NoteInfo===\n\n" + note.getContent();
        Storage.put(path, contents);
    }

    public int create(String path, NoteItem note) {
        if (Storage.exist(path)) {
            return 409;
        }
        this.set(path, note);
        return 200;
    }

    public int delete(String path) {
        Storage.delete(path);
        return 200;
    }

    public int edit(String path, NoteItem note) {
        this.set(path, note);
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

    public JsonNode checkStatus(List<String> checkList, String prefix) {
        ObjectNode node = JSON.createObject();
        for (String item : checkList) {
            String path = prefix + item;
            ObjectNode itemNode = JSON.createObject();
            if (Storage.exist(path)) {
                ObjectNode note = (ObjectNode) this.get(path);
                itemNode.set("created_at", note.get("created_at"));
                itemNode.set("updated_at", note.get("updated_at"));
            } else {
                itemNode.put("created_at", "No exists");
                itemNode.put("updated_at", "No exists");
            }
            node.set(item, itemNode);
        }
        return node;
    }
}
