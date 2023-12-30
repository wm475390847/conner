package com.sprouts.conner.annotation;

import com.sprouts.conner.config.AbstractInformConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 发送通知
 *
 * @author wangmin
 * @date 2023/7/7 11:08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InformSend {

    /**
     * 使用的通知配置
     *
     * @return 通知配置
     */
    Class<? extends AbstractInformConfig> value() default AbstractInformConfig.class;

    /**
     * 是否发送
     * 默认不发送
     *
     * @return true or false
     */
    boolean send() default true;
}
