package com.sprouts.conner.http.connector;

import com.sprouts.conner.http.Api;
import com.sprouts.conner.response.ResponseLog;
import com.sprouts.conner.config.HttpConfig;
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
}
