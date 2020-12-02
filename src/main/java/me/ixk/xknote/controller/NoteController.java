package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import java.util.regex.Pattern;
import me.ixk.xknote.annotation.JsonParam;
import me.ixk.xknote.controller.param.CheckList;
import me.ixk.xknote.controller.param.NoteItem;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.NoteServiceImpl;
import me.ixk.xknote.utils.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@PreAuthorize("isAuthenticated()")
public class NoteController {
    @Autowired
    NoteServiceImpl noteService;

    @GetMapping("")
    public ResponseEntity<Object> get(
        @RequestParam(name = "path") String path
    ) {
        JsonNode node = noteService.get(this.getPath(path));
        if (node.isInt() && node.asInt() == 404) {
            return ResponseInfo.stdError(
                "Folder not found.",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson("note", node);
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody NoteItem note) {
        if (!Pattern.matches(".+\\.(md|txt)$", note.getPath())) {
            return ResponseInfo.stdError(
                "Parameter error. (path)",
                HttpStatus.BAD_REQUEST
            );
        }
        int code = noteService.create(this.getPath(note.getPath()), note);
        if (code == 409) {
            return ResponseInfo.stdError(
                "The note already exists.",
                HttpStatus.CONFLICT
            );
        }
        return ResponseInfo.stdJson();
    }

    @DeleteMapping("")
    public ResponseEntity<Object> delete(
        @RequestParam(name = "path") String path
    ) {
        noteService.delete(this.getPath(path));
        return ResponseInfo.stdJson();
    }

    @PutMapping("")
    public ResponseEntity<Object> edit(@RequestBody NoteItem note) {
        if (!Pattern.matches(".+\\.(md|txt)$", note.getPath())) {
            return ResponseInfo.stdError(
                "Parameter error. (path)",
                HttpStatus.BAD_REQUEST
            );
        }
        int code = noteService.edit(this.getPath(note.getPath()), note);
        return ResponseInfo.stdJson();
    }

    @PutMapping({ "/rename", "/move" })
    public ResponseEntity<Object> move(
        @JsonParam(name = "old_path") String oldPath,
        @JsonParam(name = "new_path") String newPath
    ) {
        oldPath = this.getPath(oldPath);
        newPath = this.getPath(newPath);
        if ((oldPath + newPath).contains("../")) {
            return ResponseInfo.stdError(
                "You submitted a restricted character. (../)",
                HttpStatus.BAD_REQUEST
            );
        }
        if (!Pattern.matches(".+\\.(md|txt)$", newPath)) {
            return ResponseInfo.stdError(
                "Parameter error. (path)",
                HttpStatus.BAD_REQUEST
            );
        }
        int code = noteService.move(oldPath, newPath);
        if (code == 404) {
            return ResponseInfo.stdError(
                "Folder not found.",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson();
    }

    @GetMapping("/all")
    public ResponseEntity<Object> all(
        @RequestParam(name = "path") String path
    ) {
        JsonNode node = noteService.getAll(path);
        if (node.isInt() && node.asInt() == 404) {
            return ResponseInfo.stdError("Folder not found.", 404);
        }
        return ResponseInfo.stdJson("note", node);
    }

    @GetMapping("/exist")
    public ResponseEntity<Object> exist(
        @RequestParam(name = "path") String path
    ) {
        return ResponseInfo.stdJson(
            "exist",
            BooleanNode.valueOf(noteService.exist(this.getPath(path)))
        );
    }

    @PostMapping("/check")
    public ResponseEntity<Object> check(@RequestBody CheckList checkList) {
        return ResponseInfo.stdJson(
            "check_list",
            noteService.checkStatus(checkList.getCheckList(), this.getPath(""))
        );
    }

    private String getPath(String path) {
        Long id = Application.getCurrentUserId();
        return "uid_" + id + path;
    }
}
