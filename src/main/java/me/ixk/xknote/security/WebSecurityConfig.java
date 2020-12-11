package me.ixk.xknote.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Web 安全配置
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:12
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/home")
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .rememberMe()
            .tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(1440 * 60)
            .userDetailsService(userDetailService)
            .and()
            .authorizeRequests()
            // 允许所有访问，使用注解进行权限控制
            .anyRequest()
            .permitAll()
            .and()
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .ignoringAntMatchers("/logout");
    }

    @Bean
    public ClassPathTldsLoader classPathTldsLoader() {
        return new ClassPathTldsLoader();
    }

    public static class ClassPathTldsLoader {

        @Autowired
        private FreeMarkerConfigurer freeMarkerConfigurer;

        private static final String SECURITY_TLD = "/META-INF/security.tld";

        private final List<String> classPathTlds;

        public ClassPathTldsLoader(String... classPathTlds) {
            super();
            if (classPathTlds == null || classPathTlds.length == 0) {
                this.classPathTlds = Collections.singletonList(SECURITY_TLD);
            } else {
                this.classPathTlds = Arrays.asList(classPathTlds);
            }
        }

        @PostConstruct
        public void loadClassPathTlds() {
            freeMarkerConfigurer
                .getTaglibFactory()
                .setClasspathTlds(classPathTlds);
        }
    }
}
