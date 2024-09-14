package com.sprouts.quality.conner.annotation;

import com.sprouts.quality.conner.collector.ICollector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用例收集组件
 *
 * @author wangmin
 * @date 2022/8/18 17:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collector {

    Class<? extends ICollector> value() default ICollector.class;

    /**
     * 作者
     */
    String caseOwner() default "";

    /**
     * 是否允许采集
     */
    boolean enable() default true;

    /**
     * 是否发送通知
     */
    boolean sendNotify() default false;
}
