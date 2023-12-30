package com.sprouts.conner;

import com.sprouts.conner.config.*;
import com.sprouts.conner.response.ResponseLog;
import okhttp3.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 上下文配置
 *
 * @author wangmin
 * @date 2022/5/17 13:05
 */
public class Context {

    public static Map<Class<?>, Anno> map = new LinkedHashMap<>(16);

    /**
     * 当前执行的类
     */
    public static Class<?> currentExecuteClass;

    /**
     * 默认的调试模式
     */
    public static boolean debug;

    /**
     * 详细信息
     */
    public static String detailMessage;

    /**
     * 响应日志
     */
    public static ResponseLog<Response> responseLog;

    /**
     * 获取配置
     *
     * @param configClass 需要获取的配置类型{@link AbstractInformConfig}、{@link HttpConfig}、{@link ProductConfig}
     * @return 配置容器
     */
    public static <C extends IConfig> C getConfig(Class<C> configClass) {
        Anno anno = getAnno();
        if (anno == null) {
            return null;
        }
        IConfigContainer configContainer = anno.getConfigContainer();
        if (configContainer == null) {
            return null;
        }
        return configContainer.findConfig(configClass);
    }

    /**
     * 获取收集器
     *
     * @param <T> 泛型的class
     * @return AbstractCollector
     */
    public static <T> AbstractCollector getCollector() {
        Anno anno = getAnno();
        if (anno == null) {
            return null;
        }
        return anno.getAbstractCollector();
    }

    /**
     * 获取注解管理
     *
     * @param <T> 泛型的class
     * @return 注解
     */
    public static <T> Anno getAnno() {
        if (map.isEmpty()) {
            return null;
        }
        return map.get(currentExecuteClass);
    }

    public static void clearClass() {
        currentExecuteClass = null;
    }

    public static void clearMethod() {
        detailMessage = null;
        responseLog = null;
    }
}
