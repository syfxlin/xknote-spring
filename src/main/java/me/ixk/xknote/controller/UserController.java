package me.ixk.xknote.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.ixk.xknote.controller.param.ConfigParam;
import me.ixk.xknote.controller.param.UserParam;
import me.ixk.xknote.entity.UserConfig;
import me.ixk.xknote.entity.Users;
import me.ixk.xknote.http.ResponseInfo;
import me.ixk.xknote.security.UserDetailsImpl;
import me.ixk.xknote.service.impl.UserConfigServiceImpl;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.Application;
import me.ixk.xknote.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

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

    @GetMapping("/conf")
    public ResponseEntity<Object> getConfig() {
        Long id = Application.getCurrentUserId();
        UserConfig config = userConfigService.query().eq("uid", id).one();
        ObjectNode objectNode = JSON.createObject();
        objectNode.set(
            "tinymceSetting",
            JSON.parseObject(config.getTinymceSetting())
        );
        objectNode.set("aceSetting", JSON.parseObject(config.getAceSetting()));
        objectNode.set("xkSetting", JSON.parseObject(config.getXkSetting()));
        return ResponseInfo.stdJson("config", objectNode);
    }

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

    @GetMapping("")
    public ResponseEntity<Object> get() {
        UserDetailsImpl userDetails = Application.getCurrentUser();
        if (userDetails == null) {
            return ResponseInfo.stdError("Not Login", HttpStatus.UNAUTHORIZED);
        }
        return ResponseInfo.stdJson("user", userDetails.getUsers());
    }

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
