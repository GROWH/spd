package org.jrplat.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 赵腾飞 on 16-10-19.
 * 当属性添加该注解后则支持模糊查询。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportLikeQuery {
    String value() default "";
}
