package me.ixk.xknote.controller.auth;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import javax.validation.Valid;
import me.ixk.xknote.entity.UserConfig;
import me.ixk.xknote.entity.Users;
import me.ixk.xknote.http.RegisterUser;
import me.ixk.xknote.service.impl.UserConfigServiceImpl;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.Json;
import me.ixk.xknote.utils.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 注册控制器
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:14
 */
@Controller
public class RegisterController {

    @Autowired
    UsersServiceImpl usersService;

    @Autowired
    UserConfigServiceImpl userConfigService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 注册页
     *
     * @param registerUser 注册的用户信息
     *
     * @return 注册页位置
     */
    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String index(RegisterUser registerUser) {
        return "register";
    }

    /**
     * 注册
     *
     * @param registerUser 注册用户信息
     * @param result       验证信息
     *
     * @return 跳转 URL
     */
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = Exception.class)
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
        // 新建用户
        usersService.save(user);
        Users newUser = usersService
            .query()
            .eq("username", user.getUsername())
            .one();
        // 读取默认配置
        ObjectNode settings = Json.parseObject(
            Storage.readFormClasspath("/settings.json")
        );
        UserConfig userConfig = UserConfig
            .builder()
            .uid(newUser.getId())
            .tinymceSetting(settings.get("tinymce_setting").toString())
            .aceSetting(settings.get("ace_setting").toString())
            .xkSetting(settings.get("xk_setting").toString())
            .build();
        // 存储默认配置
        userConfigService.save(userConfig);
        // 创建用户对应文件夹
        Storage.makeDirectory("uid_" + newUser.getId());
        return "redirect:/login";
    }
}
