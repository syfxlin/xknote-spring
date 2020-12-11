package me.ixk.xknote.controller;

import java.time.LocalDateTime;
import me.ixk.xknote.annotation.JsonParam;
import me.ixk.xknote.entity.GitInfo;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.service.impl.GitInfoServiceImpl;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Git 信息控制器
 *
 * @author Otstar Lin
 * @date 2020/12/3 下午 10:45
 */
@RestController
@RequestMapping("/api/repo")
public class GitInfoController {

    @Autowired
    GitInfoServiceImpl gitInfoService;

    @Autowired
    Crypt crypt;

    /**
     * 获取全局 Git 配置
     *
     * @return 配置信息
     */
    @GetMapping("/git")
    public ResponseEntity<Object> getConfig() {
        final Long id = Application.getCurrentUserId();
        final GitInfo info = gitInfoService.query().eq("uid", id).one();
        if (info == null) {
            return ResponseInfo.stdError(
                "You need to set up Git information",
                HttpStatus.NOT_FOUND
            );
        }
        return ResponseInfo.stdJson("config", info);
    }

    /**
     * 设置全局 Git 信息
     *
     * @param gitName     Git 用户名
     * @param gitPassword Git 密码
     * @param gitEmail    Git 邮箱
     *
     * @return 是否成功
     */
    @PostMapping("/git")
    public ResponseEntity<Object> setConfig(
        @JsonParam(name = "git_name") String gitName,
        @JsonParam(name = "git_password") String gitPassword,
        @JsonParam(name = "git_email") String gitEmail
    ) {
        final Long id = Application.getCurrentUserId();
        gitPassword = crypt.encrypt(gitPassword);
        final GitInfo info = gitInfoService.query().eq("uid", id).one();
        if (info == null) {
            final GitInfo gitInfo = GitInfo
                .builder()
                .uid(id)
                .gitName(gitName)
                .gitEmail(gitEmail)
                .gitPassword(gitPassword)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            gitInfoService.save(gitInfo);
        } else {
            info.setGitName(gitName);
            info.setGitEmail(gitEmail);
            info.setGitPassword(gitPassword);
            info.setUpdatedAt(LocalDateTime.now());
            gitInfoService.saveOrUpdate(info);
        }
        return ResponseInfo.stdJson();
    }
}
