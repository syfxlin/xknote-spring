package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.node.BooleanNode;
import me.ixk.xknote.annotation.JsonParam;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.FolderServiceImpl;
import me.ixk.xknote.service.impl.ReadMode;
import me.ixk.xknote.utils.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件夹控制器
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:24
 */
@RestController
@RequestMapping("/api/folders")
@PreAuthorize("isAuthenticated()")
public class FolderController {

    @Autowired
    FolderServiceImpl folderService;

    /**
     * 获取文件夹和文件信息
     *
     * @param authentication 授权信息
     *
     * @return 文件夹和文件信息
     */
    @GetMapping
    public ResponseEntity<Object> get(Authentication authentication) {
        return this.get(ReadMode.ALL);
    }

    /**
     * 只获取文件夹信息
     *
     * @param authentication 授权信息
     *
     * @return 文件夹信息
     */
    @GetMapping("/only")
    public ResponseEntity<Object> only(Authentication authentication) {
        return this.get(ReadMode.ONLY);
    }

    /**
     * 扁平化文件夹信息
     *
     * @param authentication 授权信息
     *
     * @return 扁平化文件夹信息
     */
    @GetMapping("/flat")
    public ResponseEntity<Object> flat(Authentication authentication) {
        return this.get(ReadMode.FLAT);
    }

    /**
     * 新建文件夹
     *
     * @param path 路径
     *
     * @return 正常响应
     */
    @PostMapping
    public ResponseEntity<Object> create(@JsonParam String path) {
        int code = folderService.create(this.getPath(path));
        if (code == 409) {
            return ResponseInfo.stdError(
                "The folder already exists.",
                HttpStatus.CONFLICT
            );
        }
        return ResponseInfo.stdJson();
    }

    /**
     * 删除文件夹
     *
     * @param path 路径
     *
     * @return 正常响应
     */
    @DeleteMapping
    public ResponseEntity<Object> delete(
        @RequestParam(name = "path") String path
    ) {
        int code = folderService.delete(this.getPath(path));
        return ResponseInfo.stdJson();
    }

    /**
     * 移动，重命名文件夹
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     *
     * @return 正常响应
     */
    @PutMapping({ "", "/move", "/rename" })
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
        int code = folderService.move(oldPath, newPath);
        if (code == 404) {
            return ResponseInfo.stdError(
                "Folder not found.",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson();
    }

    /**
     * 是否存在文件夹
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
