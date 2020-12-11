package me.ixk.xknote.http;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import me.ixk.xknote.utils.Application;

/**
 * 注册用户对象
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:11
 */
@Data
public class RegisterUser {

    @NotNull
    @Size(min = 2, max = 50)
    private String username;

    @NotNull
    @Size(min = 2, max = 50)
    private String nickname;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Size(min = 6, max = 50)
    private String password_confirmation;

    @AssertTrue(message = "两次密码不一致")
    private boolean isPasswordEquals() {
        return (
            this.password != null && password.equals(this.password_confirmation)
        );
    }

    @AssertTrue(message = "用户名已存在")
    private boolean isUnique() {
        if (this.username == null) {
            return false;
        }
        return (
            Application
                .getBean(UsersServiceImpl.class)
                .query()
                .eq("username", this.username)
                .count() ==
            0
        );
    }
}
