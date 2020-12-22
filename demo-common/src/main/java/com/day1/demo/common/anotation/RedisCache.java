package com.day1.demo.common.anotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: LinHangHui
 * @Date: 2020/12/18 11:05
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisCache {

    /**
     * 缓存key前缀
     *
     * @return
     */
    @AliasFor("prefix")
    String value() default "";

    /**
     * 缓存key前缀
     *
     * @return
     */
    @AliasFor("value")
    String prefix() default "";


    /**
     * 缓存key,多入参时支持#指定,否则全部参数md5作为key
     * 如入参为对象,则支持#object.field方式获取
     *
     * @return
     */
    String key() default "";

    /**
     * 缓存有效时间
     *
     * @return
     */
    int live() default 60;


    /**
     * 缓存时间单位
     *
     * @return
     */
    TimeUnit TIME_UNIT() default TimeUnit.SECONDS;

    /**
     * 开启双缓存
     *
     * @return
     */
    boolean doubleCache() default true;

    /**
     * 回源锁有效时间,单位(秒)
     *
     * @return
     */
    long lockLive() default 10;

    /**
     * redis键值是否需要代入参数
     *
     * @return
     */
    boolean isEmbedKey() default false;

    /**
     * 为空则不缓存
     *
     * @return
     */
    boolean noNullCache() default true;

    /**
     * 注解是否用于清除缓存
     *
     * @return
     */
    boolean doRemoveCache() default false;
}
