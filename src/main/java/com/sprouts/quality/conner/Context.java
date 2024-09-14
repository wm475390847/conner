package com.sprouts.quality.conner;

import com.sprouts.quality.conner.collector.ICollector;
import com.sprouts.quality.conner.config.IConfigContainer;
import com.sprouts.quality.conner.response.ResponseLog;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局上下文处理
 */
@Slf4j
public class Context {

    /**
     * 详细信息
     */
    public static String detailMessage;

    /**
     * 响应日志
     */
    public static ResponseLog<Response> responseLog;

    private static final Map<Class<?>, Anno> ANNO_MAP = new ConcurrentHashMap<>();
    public static boolean DEBUG;

    public static <T> Map<Anno, ? extends ICollector> getCollector(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }
        return Optional.ofNullable(getAnno(tClass))
                .map(anno -> Optional.ofNullable(anno.getCaseCollector())
                        .flatMap(collector -> Optional.ofNullable(collector.value())
                                .filter(value -> !value.equals(ICollector.class))
                                .map(value -> {
                                    try {
                                        return Collections.singletonMap(anno, value.newInstance());
                                    } catch (InstantiationException | IllegalAccessException e) {
                                        log.info("收集器获取错误: {}", e.getMessage());
                                        return null;
                                    }
                                }))
                        .orElse(Collections.emptyMap()))
                .orElse(null);
    }


    public static <T> IConfigContainer getContainer(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }
        return Optional.ofNullable(getAnno(tClass))
                .flatMap(anno -> Optional.ofNullable(anno.getConfigContainer()))
                .orElse(null);
    }

    public static <T> Anno getAnno(Class<T> tClass) {
        return ANNO_MAP.get(tClass);
    }

    public static <T> void setAnno(Class<T> tClass, Anno anno) {
        ANNO_MAP.put(tClass, anno);
    }
}
