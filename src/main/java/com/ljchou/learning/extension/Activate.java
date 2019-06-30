package com.ljchou.learning.extension;

import java.lang.annotation.*;

/**
 * 自动激活
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {

    String[] group() default {};

    String[] value() default {};

    int order() default 0;

}
