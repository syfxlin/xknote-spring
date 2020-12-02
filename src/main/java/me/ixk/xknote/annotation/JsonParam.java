package me.ixk.xknote.annotation;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;

/**
 * JsonParam
 *
 * @author Otstar Lin
 * @date 2020/11/17 下午 5:46
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}