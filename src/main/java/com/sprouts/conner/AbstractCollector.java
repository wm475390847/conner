package com.sprouts.conner;

import com.sprouts.conner.utils.Property;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

/**
 * 用例收集器基类
 * <P>这里只定义一下方法的执行方向不指定具体执行内容，子类来实现具体的方法</P>
 *
 * @author wangmin
 * @date 2022/5/27 10:01
 */
@Slf4j
public abstract class AbstractCollector {

    /**
     * 初始化case
     *
     * @param method 测试方法
     * @param env    执行环境
     * @return 用例数据
     */
    public abstract Object initCase(ITestNGMethod method, String env);

    /**
     * 保存数据
     *
     * @param info      用例数据
     * @param throwable 错误信息
     */
    public abstract void updateCase(Object info, Throwable throwable);

    /**
     * 发送通知
     *
     * @param info 用例数据
     */
    public abstract void sendInform(Object info);

    /**
     * 初始化用例数据
     *
     * @param iTestResult 测试结果
     * @return 用例数据
     */
    public Object initCase(ITestResult iTestResult) {
        ITestNGMethod method = iTestResult.getMethod();
        String env = Property.parse().getProperty("env");
        log.info("==> env: {}, caseName: {}, 调试模式: {}", env, method.getMethodName(), Context.debug);
        return initCase(method, env);
    }

    public void updateCase(Object info, ITestResult iTestResult) {
        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            return;
        }
        updateCase(info, throwable);
    }
}
