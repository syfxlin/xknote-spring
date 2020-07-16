package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.node.BooleanNode;
import me.ixk.xknote.controller.param.OldNewPathParam;
import me.ixk.xknote.controller.param.PathParam;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.FolderServiceImpl;
import me.ixk.xknote.service.impl.ReadMode;
import me.ixk.xknote.utils.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/folders")
@PreAuthorize("isAuthenticated()")
public class FolderController {
    @Autowired
    FolderServiceImpl folderService;

    @GetMapping("")
    public ResponseEntity<Object> get(Authentication authentication) {
        return this.get(ReadMode.ALL);
    }

    @GetMapping("/only")
    public ResponseEntity<Object> only(Authentication authentication) {
        return this.get(ReadMode.ONLY);
    }

    @GetMapping("/flat")
    public ResponseEntity<Object> flat(Authentication authentication) {
        return this.get(ReadMode.FLAT);
    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody PathParam path) {
        int code = folderService.create(this.getPath(path.getPath()));
        if (code == 409) {
            return ResponseInfo.stdError(
                "The folder already exists.",
                HttpStatus.CONFLICT
            );
        }
        return ResponseInfo.stdJson();
    }

    @DeleteMapping("")
    public ResponseEntity<Object> delete(
        @RequestParam(name = "path") String path
    ) {
        int code = folderService.delete(this.getPath(path));
        return ResponseInfo.stdJson();
    }

    @PutMapping({ "", "/move", "/rename" })
    public ResponseEntity<Object> move(@RequestBody OldNewPathParam path) {
        String oldPath = this.getPath(path.getOldPath());
        String newPath = this.getPath(path.getNewPath());
        if ((oldPath + newPath).contains("../")) {
            return ResponseInfo.stdError(
                "You submitted a restricted character. (../)",
                HttpStatus.BAD_REQUEST
            );
        }
        int code = folderService.move(oldPath, newPath);
        if (code == 404) {
            return ResponseInfo.stdError(
                "Folder not found.",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson();
    }

    @GetMapping("/exist")
    public ResponseEntity<Object> exist(
        @RequestParam(name = "path") String path
    ) {
        return ResponseInfo.stdJson(
            "exist",
            BooleanNode.valueOf(folderService.exist(this.getPath(path)))
        );
    }

    private ResponseEntity<Object> get(ReadMode mode) {
        return ResponseInfo.stdJson(
            "folders",
            folderService.get(this.getPath(), true, mode)
        );
    }

    private String getPath() {
        return this.getPath("");
    }

    private String getPath(String path) {
        Long id = Application.getCurrentUserId();
        return "uid_" + id + path;
    }
}
