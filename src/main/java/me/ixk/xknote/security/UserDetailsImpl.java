package me.ixk.xknote.security;

import java.time.LocalDateTime;
import java.util.Collection;
import me.ixk.xknote.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * 用户描述对象
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:12
 */
public class UserDetailsImpl extends User {

    private final Users users;

    public UserDetailsImpl(Users users, String authorities) {
        this(
            users,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        );
    }

    public UserDetailsImpl(
        Users users,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(users.getUsername(), users.getPassword(), authorities);
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

    public Long getId() {
        return users.getId();
    }

    public String getNickname() {
        return users.getNickname();
    }

    public String getEmail() {
        return users.getEmail();
    }

    public LocalDateTime getEmailVerifiedAt() {
        return users.getEmailVerifiedAt();
    }

    public String getRememberToken() {
        return users.getRememberToken();
    }

    public LocalDateTime getCreatedAt() {
        return users.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return users.getUpdatedAt();
    }
}
