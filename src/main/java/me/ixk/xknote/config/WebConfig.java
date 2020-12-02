package me.ixk.xknote.config;

import java.util.List;
import me.ixk.xknote.resolver.JsonArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author Otstar Lin
 * @date 2020/11/17 下午 5:52
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(
        List< HandlerMethodArgumentResolver > resolvers
    ) {
        resolvers.add(new JsonArgumentResolver());
    }
}