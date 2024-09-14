package com.sprouts.quality.conner.http;

import com.sprouts.quality.conner.config.HttpConfig;
import com.sprouts.quality.conner.response.ResponseLog;
import okhttp3.Response;

/**
 * @author wangmin
 * @date 2022/10/13 10:31
 */
public interface IConnector<T> {

    /**
     * 放入api对象
     *
     * @param api api对象
     * @return IConnector<T>
     */
    IConnector<T> api(Api api);

    /**
     * 放入http的配置
     *
     * @param httpConfig http配置
     * @return IConnector<T>
     */
    IConnector<T> config(HttpConfig httpConfig);

    /**
     * 获取api
     *
     * @return Api对象
     */
    Api getApi();

    /**
     * 执行
     *
     * @return 请求响应
     */
    Response execute();

    /**
     * 获取日志
     *
     * @return 请求日志
     */
    ResponseLog<T> getLog();

    /**
     * 插入当前运行的类
     *
     * @param rClass 当前运行的类
     */
    <R> IConnector<T> setCurrentExecuteClass(Class<R> rClass);

    /**
     * 忽略请求校验
     *
     * @return IConnector<T>
     */
    IConnector<T> ignoreCheck();
}
