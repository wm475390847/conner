package com.sprouts.quality.conner.http;

import com.sprouts.quality.conner.Context;
import com.sprouts.quality.conner.config.HttpConfig;
import com.sprouts.quality.conner.config.IConfigContainer;
import com.sprouts.quality.conner.exception.ConnerException;
import com.sprouts.quality.conner.response.ResponseLog;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractConnector implements IConnector<Response> {

    private boolean ignoreCheck = false;
    private Class<Object> rClass;
    private Response response;
    private HttpConfig httpConfig;
    private Api api;

    @Override
    public IConnector<Response> api(Api api) {
        this.api = api;
        return this;
    }

    @Override
    public IConnector<Response> config(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
        return this;
    }

    /**
     * 执行请求
     * <p>传入公共头部配置&完整的url&每个接口的属性来调用一个http/https请求
     *
     * @return 响应体
     */
    @Override
    public Response execute() {
        // 获取api信息
        Api api = getApi();
        // 获取接口
        String url = api.getUrl();
        // 构建一个新的请求
        Request.Builder builder = new Request.Builder();
        // 为请求添加请求头
        api.getHeaders().entrySet().stream().filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .forEach(e -> builder.header(e.getKey(), e.getValue()));
        // 将一些参数放入请求中
        buildRequest(builder, api);
        // 完成构建
        Request request = builder.url(url).build();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient
                .Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        if (isHttps(url) && api.getIgnoreSsl()) {
            ignoreSsl(okHttpClientBuilder);
        }

        if (StringUtils.isNotEmpty(api.getIpaddress())) {
            okHttpClientBuilder.dns(new CustomDns(api.getHostname(), api.getIpaddress()));
        }
        try {
            response = okHttpClientBuilder.build().newCall(request).execute();
        } catch (IOException | NullPointerException e) {
            throw new ConnerException(e.getMessage());
        }
        return response;
    }

    @Override
    public IConnector<Response> ignoreCheck() {
        this.ignoreCheck = true;
        return this;
    }

    @Override
    public ResponseLog<Response> getLog() {
        Optional.ofNullable(response).orElseThrow(() -> new ConnerException("请求响应为空"));
        if (!ignoreCheck) {
            Optional.of(response).filter(Response::isSuccessful).orElseThrow(() -> new ConnerException(response.toString()));
        }
        ResponseLog<Response> log = new ResponseLog<>();
        Optional.ofNullable(api).orElseThrow(() -> new ConnerException("api为空"));
        return log.setStartTime(response.sentRequestAtMillis()).setEndTime(response.receivedResponseAtMillis()).setResponse(response).setApi(api);
    }

    /**
     * 获取api
     *
     * @return api
     */
    @Override
    public Api getApi() {
        Optional.ofNullable(api).orElseThrow(() -> new ConnerException("api为空，请使用IConnector.api()方法放入api"));
        IConfigContainer container = Context.getContainer(rClass);
        HttpConfig httpConfig = this.httpConfig == null ? container == null ? null : container.getConfig(HttpConfig.class) : this.httpConfig;
        this.api.setHttpConfig(httpConfig);
        return api;
    }

    @Override
    public <R> IConnector<Response> setCurrentExecuteClass(Class<R> rClass) {
        this.rClass = (Class<Object>) rClass;
        return this;
    }

    /**
     * 构建请求
     * <P>需要子类实现
     *
     * @param builder 请求构建者
     * @param api     Api类
     */
    protected abstract void buildRequest(Request.Builder builder, Api api);


    /**
     * 是否忽略ssl证书
     *
     * @param okHttpClientBuilder okHttpClientBuilder
     */
    private void ignoreSsl(final OkHttpClient.Builder okHttpClientBuilder) {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{TRUST_MANAGER}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("SSLContext初始化异常:{}", e.getMessage());
        }
        SSLSocketFactory sslSocketFactory = sslContext != null ? sslContext.getSocketFactory() : null;
        if (sslSocketFactory != null) {
            okHttpClientBuilder.sslSocketFactory(sslSocketFactory, TRUST_MANAGER).hostnameVerifier((hostname, session) -> true);
        }
    }

    private static final X509TrustManager TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    private static boolean isHttps(String url) {
        return url.contains("https://");
    }
}
