package me.ixk.xknote.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * 用户信息
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 1:19
 */
@Data
public class UserParam {

    @NotNull
    @Size(min = 1, max = 50)
    private String email;

    @NotNull
    @Size(min = 1, max = 50)
    private String nickname;

    @NotNull
    @Size(min = 1, max = 50)
    @JsonProperty("old_password")
    private String oldPassword;

    @NotNull
    @Size(min = 1, max = 50)
    private String password;

    @NotNull
    @Size(min = 1, max = 50)
    @JsonProperty("password_confirmation")
    private String passwordConfirmation;

    @AssertTrue(message = "两次密码不一致")
    private boolean isPasswordEquals() {
        return (
            this.password != null && password.equals(this.passwordConfirmation)
        );
    }
}
