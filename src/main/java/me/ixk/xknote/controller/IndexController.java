package me.ixk.xknote.controller;

import me.ixk.xknote.entity.XknoteData;
import me.ixk.xknote.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String welcome() {
        return "welcome";
    }

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
