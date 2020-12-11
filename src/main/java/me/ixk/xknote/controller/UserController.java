package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import javax.validation.Valid;
import me.ixk.xknote.controller.param.ConfigParam;
import me.ixk.xknote.controller.param.UserParam;
import me.ixk.xknote.entity.UserConfig;
import me.ixk.xknote.entity.Users;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.security.UserDetailsImpl;
import me.ixk.xknote.service.impl.UserConfigServiceImpl;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:07
 */
@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    UserConfigServiceImpl userConfigService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsersServiceImpl usersService;

    /**
     * 获取用户配置
     *
     * @return 用户配置
     */
    @GetMapping("/conf")
    public ResponseEntity<Object> getConfig() {
        Long id = Application.getCurrentUserId();
        UserConfig config = userConfigService.query().eq("uid", id).one();
        ObjectNode objectNode = Json.createObject();
        objectNode.set(
            "tinymceSetting",
            Json.parseObject(config.getTinymceSetting())
        );
        objectNode.set("aceSetting", Json.parseObject(config.getAceSetting()));
        objectNode.set("xkSetting", Json.parseObject(config.getXkSetting()));
        return ResponseInfo.stdJson("config", objectNode);
    }

    /**
     * 设置用户配置
     *
     * @param config 用户配置
     *
     * @return 正常返回
     */
    @PutMapping("/conf")
    public ResponseEntity<Object> setConfig(@RequestBody ConfigParam config) {
        Long id = Application.getCurrentUserId();
        UserConfig userConfig = userConfigService.query().eq("uid", id).one();
        userConfig.setTinymceSetting(config.getTinymceSetting().toString());
        userConfig.setAceSetting(config.getAceSetting().toString());
        userConfig.setXkSetting(config.getXkSetting().toString());
        userConfigService.saveOrUpdate(userConfig);
        return ResponseInfo.stdJson();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("")
    public ResponseEntity<Object> get() {
        UserDetailsImpl userDetails = Application.getCurrentUser();
        if (userDetails == null) {
            return ResponseInfo.stdError("Not Login", HttpStatus.UNAUTHORIZED);
        }
        return ResponseInfo.stdJson("user", userDetails.getUsers());
    }

    /**
     * 修改用户信息
     *
     * @param user   用户信息
     * @param result 验证信息
     *
     * @return 修改后的用户信息
     */
    @PutMapping("")
    public ResponseEntity<Object> edit(
        @Valid @RequestBody UserParam user,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseInfo.stdError(
                result.getAllErrors().toString(),
                HttpStatus.BAD_REQUEST
            );
        }
        UserDetailsImpl userDetails = Application.getCurrentUser();
        if (
            userDetails == null ||
            passwordEncoder.matches(
                user.getOldPassword(),
                userDetails.getPassword()
            )
        ) {
            return ResponseInfo.stdError(
                "Old password does not match.",
                HttpStatus.UNAUTHORIZED
            );
        }
        Users users = userDetails.getUsers();
        users.setNickname(user.getNickname());
        users.setEmail(user.getEmail());
        users.setPassword(passwordEncoder.encode(user.getPassword()));
        users.setUpdatedAt(LocalDateTime.now());
        usersService.updateById(users);
        return ResponseInfo.stdJson("user", users);
    }
}
