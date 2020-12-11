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

    /**
     * 添加参数解析器
     *
     * @param resolvers 解析器
     */
    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> resolvers
    ) {
        // 添加 Json 解析器
        resolvers.add(new JsonArgumentResolver());
    }
}
