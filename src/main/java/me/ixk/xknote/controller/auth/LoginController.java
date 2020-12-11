package me.ixk.xknote.controller.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录控制器
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:13
 */
@Controller
public class LoginController {

    /**
     * 登录页
     *
     * @return 模板位置
     */
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String index() {
        return "login";
    }
}
