package me.ixk.xknote.config;

import cn.willingxyz.restdoc.core.config.RestDocConfig;
import cn.willingxyz.restdoc.springswagger3.EnableSwagger3;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 *
 * @author Otstar Lin
 * @date 2020/12/11 上午 10:21
 */
@Configuration
@EnableSwagger3
public class SwaggerConfig {

    @Bean
    public RestDocConfig _swaggerConfig() {
        return RestDocConfig
            .builder()
            .apiTitle("XK-Note 云笔记")
            .apiDescription("一个集各种神奇功能的云笔记")
            .apiVersion("2.0.0")
            .packages(Collections.singletonList("me.ixk.xknote"))
            .build();
    }
}
