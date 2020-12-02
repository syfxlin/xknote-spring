package me.ixk.xknote.annotation;

import java.lang.annotation.*;

/**
 * RequestJson
 *
 * @author Otstar Lin
 * @date 2020/11/17 下午 5:46
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {
}
