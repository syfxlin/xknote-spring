package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import me.ixk.xknote.annotation.JsonParam;
import me.ixk.xknote.entity.GitConfig;
import me.ixk.xknote.entity.GitUserInfo;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.GitRepoServiceImpl;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.Json;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Git 仓库操作
 *
 * @author Otstar Lin
 * @date 2020/12/2 下午 8:10
 */
@RestController
@RequestMapping("/api/repo")
@PreAuthorize("isAuthenticated()")
public class GitRepoController {

    @Autowired
    GitRepoServiceImpl gitRepoService;

    /**
     * Git init 或 clone
     *
     * @param path        相对路径
     * @param repo        仓库 remote 地址
     * @param initOrClone init 或 clone
     * @param gitName     Git 用户名
     * @param gitPassword Git 密码
     * @param gitEmail    Git 邮箱
     *
     * @return 是否成功
     */
    @PostMapping
    public ResponseEntity<Object> initClone(
        @JsonParam(name = "path") String path,
        @JsonParam(name = "repo") String repo,
        @JsonParam(name = "init_or_clone") String initOrClone,
        @JsonParam(name = "git_name", required = false) String gitName,
        @JsonParam(name = "git_password", required = false) String gitPassword,
        @JsonParam(name = "git_email", required = false) String gitEmail
    ) {
        final Long id = Application.getCurrentUserId();
        if (path.lastIndexOf("/") != 0) {
            return ResponseInfo.stdError(
                "Parameter error. (path)",
                HttpStatus.BAD_REQUEST
            );
        }
        if (gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is already a repo under the path",
                HttpStatus.CONFLICT
            );
        }
        GitUserInfo user = null;
        if (gitName != null && gitPassword != null && gitEmail != null) {
            user = new GitUserInfo(gitName, gitEmail, gitPassword);
        }
        int code = 200;
        if ("init".equals(initOrClone)) {
            code = gitRepoService.init(path, id, repo, user);
        } else {
            code = gitRepoService.cloneRepo(path, id, repo, user);
        }
        if (code == 404) {
            return ResponseInfo.stdError(
                "You need to set up Git information",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson();
    }

    /**
     * Git pull
     *
     * @param path 路径
     *
     * @return 是否成功
     */
    @GetMapping
    public ResponseEntity<Object> pull(
        @RequestParam(name = "path") String path
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        gitRepoService.pull(path, id);
        return ResponseInfo.stdJson();
    }

    /**
     * Git push
     *
     * @param path  路径
     * @param force 是否强制推送
     *
     * @return 是否成功
     */
    @PutMapping
    public ResponseEntity<Object> push(
        @JsonParam(name = "path") String path,
        @JsonParam(name = "force", defaultValue = "false") boolean force
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        final int code = gitRepoService.push(path, id, force);
        if (code == 202) {
            return ResponseInfo.stdJson("message", "Everything up-to-date");
        }
        return ResponseInfo.stdJson();
    }

    /**
     * 获取当前仓库配置
     *
     * @param path 路径
     *
     * @return 当前仓库配置
     */
    @GetMapping("/conf")
    public ResponseEntity<Object> getConfig(
        @RequestParam(name = "path") String path
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        final GitConfig config = gitRepoService.config(path, id);
        if (config == null) {
            return ResponseInfo.stdError(
                "The settings are not found in the current repo, please reset them.",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson("config", config);
    }

    /**
     * 设置当前仓库配置
     *
     * @param path        路径
     * @param repo        仓库 remote 地址
     * @param gitName     Git 用户名
     * @param gitPassword Git 密码
     * @param gitEmail    Git 邮箱
     *
     * @return 是否成功
     */
    @PutMapping("/conf")
    public ResponseEntity<Object> setConfig(
        @JsonParam(name = "path") String path,
        @JsonParam(name = "repo", required = false) String repo,
        @JsonParam(name = "git_name", required = false) String gitName,
        @JsonParam(name = "git_password", required = false) String gitPassword,
        @JsonParam(name = "git_email", required = false) String gitEmail
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        GitUserInfo user = null;
        if (gitName != null && gitPassword != null) {
            user = new GitUserInfo(gitName, gitEmail, gitPassword);
        }
        if (repo != null) {
            gitRepoService.configRemote(path, id, repo, user);
        }
        if (gitEmail != null) {
            gitRepoService.configInfo(path, id, user);
        }
        return ResponseInfo.stdJson();
    }

    /**
     * commit 日志
     *
     * @param path 路径
     * @param file 文件
     *
     * @return 指定文件或全局 commit 日志
     */
    @GetMapping("/log")
    public ResponseEntity<Object> log(
        @RequestParam(name = "path") String path,
        @RequestParam(name = "file", required = false) String file
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        final Iterator<RevCommit> iterator = gitRepoService
            .log(path, id, file)
            .iterator();
        ArrayNode node = Json.createArray();
        while (iterator.hasNext()) {
            RevCommit next = iterator.next();
            ObjectNode obj = Json.createObject();
            obj.put("commit", next.getName().substring(0, 7));
            obj.put(
                "date",
                DateFormat
                    .getInstance()
                    .format(new Date(next.getCommitTime() * 1000L))
            );
            obj.put("message", next.getShortMessage());
            node.add(obj);
        }
        return ResponseInfo.stdJson("logs", node);
    }

    /**
     * 历史变更对比
     *
     * @param path   路径
     * @param commit commit hash
     * @param file   文件
     *
     * @return 获取文件或全局的历史变更
     */
    @GetMapping("/diff")
    public ResponseEntity<Object> diff(
        @RequestParam(name = "path") String path,
        @RequestParam(name = "commit") String commit,
        @RequestParam(name = "file") String file
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson(
            "diffs",
            gitRepoService.diff(path, id, commit, file)
        );
    }

    /**
     * 回滚
     *
     * @param path   路径
     * @param commit commit hash
     * @param file   文件
     *
     * @return 是否回滚成功
     */
    @PostMapping("/rollback")
    public ResponseEntity<Object> rollback(
        @JsonParam(name = "path") String path,
        @JsonParam(name = "commit") String commit,
        @JsonParam(name = "file") String file
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        gitRepoService.rollback(path, id, commit, file);
        return ResponseInfo.stdJson();
    }

    /**
     * 仓库变更状态
     *
     * @param path 路径
     *
     * @return 当前仓库变更状态
     */
    @GetMapping("/status")
    public ResponseEntity<Object> status(
        @RequestParam(name = "path") String path
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        final List<String> status = gitRepoService.status(path, id);
        return ResponseInfo.stdJson("status", status);
    }

    /**
     * 获取全局历史更改
     *
     * @param path 路径
     *
     * @return 历史更改
     */
    @GetMapping("/diff/all")
    public ResponseEntity<Object> allDiff(
        @RequestParam(name = "path") String path
    ) {
        final Long id = Application.getCurrentUserId();
        if (!gitRepoService.check(path, id)) {
            return ResponseInfo.stdError(
                "There is no repo under this path",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson("diffs", gitRepoService.diff(path, id));
    }
}
