package me.ixk.xknote.controller.auth;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import javax.validation.Valid;
import me.ixk.xknote.entity.UserConfig;
import me.ixk.xknote.entity.Users;
import me.ixk.xknote.http.RegisterUser;
import me.ixk.xknote.service.impl.UserConfigServiceImpl;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.JSON;
import me.ixk.xknote.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    UsersServiceImpl usersService;

    @Autowired
    UserConfigServiceImpl userConfigService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String index(RegisterUser registerUser) {
        return "register";
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional
    public String register(
        @Valid RegisterUser registerUser,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return "register";
        }
        Users user = Users
            .builder()
            .username(registerUser.getUsername())
            .nickname(registerUser.getNickname())
            .email(registerUser.getEmail())
            .password(passwordEncoder.encode(registerUser.getPassword()))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        usersService.save(user);
        Users newUser = usersService
            .query()
            .eq("username", user.getUsername())
            .one();
        ObjectNode settings = JSON.parseObject(
            Storage.readFormClasspath("/settings.json")
        );
        UserConfig userConfig = UserConfig
            .builder()
            .uid(newUser.getId())
            .tinymceSetting(settings.get("tinymce_setting").toString())
            .aceSetting(settings.get("ace_setting").toString())
            .xkSetting(settings.get("xk_setting").toString())
            .build();
        userConfigService.save(userConfig);
        return "redirect:/login";
    }
}
