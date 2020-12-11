package me.ixk.xknote.controller;

import me.ixk.xknote.entity.XknoteData;
import me.ixk.xknote.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制器
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:03
 */
@Controller
public class IndexController {

    /**
     * 欢迎页
     *
     * @return 欢迎页
     */
    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String welcome() {
        return "welcome";
    }

    /**
     * 主页
     *
     * @param model          Model
     * @param authentication 授权信息
     *
     * @return 主页
     */
    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String home(Model model, Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute(
            "xknoteData",
            XknoteData
                .builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .xknoteName("XK-Note")
                .documentExt("md|txt")
                .build()
        );
        return "home";
    }
}
