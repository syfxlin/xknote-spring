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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 笔记控制器
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:04
 */
@RestController
@RequestMapping("/api/notes")
@PreAuthorize("isAuthenticated()")
public class NoteController {

    @Autowired
    NoteServiceImpl noteService;

    /**
     * 获取笔记信息
     *
     * @param path 路径
     *
     * @return 笔记信息
     */
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

    /**
     * 新建笔记
     *
     * @param note 笔记信息
     *
     * @return 正常返回
     */
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

    /**
     * 删除笔记
     *
     * @param path 路径
     *
     * @return 正常返回
     */
    @DeleteMapping("")
    public ResponseEntity<Object> delete(
        @RequestParam(name = "path") String path
    ) {
        noteService.delete(this.getPath(path));
        return ResponseInfo.stdJson();
    }

    /**
     * 修改笔记
     *
     * @param note 笔记信息
     *
     * @return 正常返回
     */
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

    /**
     * 重命名或移动
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     *
     * @return 正常返回
     */
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

    /**
     * 获取所有笔记
     *
     * @param path 路径
     *
     * @return 所有笔记
     */
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

    /**
     * 是否存在笔记
     *
     * @param path 路径
     *
     * @return 是否存在
     */
    @GetMapping("/exist")
    public ResponseEntity<Object> exist(
        @RequestParam(name = "path") String path
    ) {
        return ResponseInfo.stdJson(
            "exist",
            BooleanNode.valueOf(noteService.exist(this.getPath(path)))
        );
    }

    /**
     * 检查本地和云端差异
     *
     * @param checkList 检查列表
     *
     * @return 云端列表
     */
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
