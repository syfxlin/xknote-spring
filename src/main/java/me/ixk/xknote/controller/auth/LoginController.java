package me.ixk.xknote.controller.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String index() {
        return "login";
    }
}
