package me.ixk.xknote.security;

import me.ixk.xknote.entity.Users;
import me.ixk.xknote.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 为 Spring Security 提供用户信息
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:12
 */
@Configuration
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UsersServiceImpl usersService;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        Users user = this.usersService.query().eq("username", username).one();
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        String authorities = user.getId() == 1 ? "admin,user" : "user";
        return new UserDetailsImpl(user, authorities);
    }
}
