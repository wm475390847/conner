package com.sprouts.conner.listener;

import com.sprouts.conner.AbstractCollector;
import com.sprouts.conner.Anno;
import com.sprouts.conner.Context;
import com.sprouts.conner.annotation.Collector;
import com.sprouts.conner.annotation.Container;
import com.sprouts.conner.config.IConfigContainer;
import com.sprouts.conner.exception.ListenerException;
import com.sprouts.conner.utils.Property;
import lombok.extern.slf4j.Slf4j;
import org.testng.*;

/**
 * 执行监听器
 *
 * @author wangmin
 * @date 2022/1/24 5:38 下午
 */
@Slf4j
public class ExecuteListener implements ITestListener, IClassListener {
    private Object info;

    /**
     * 方法执行之前执行
     *
     * @param iTestResult iTestResult
     */
    @Override
    public void onTestStart(ITestResult iTestResult) {
        AbstractCollector collector = Context.getCollector();
        if (collector != null) {
            info = collector.initCase(iTestResult);
        }
    }

    /**
     * 用例成功后执行
     *
     * @param iTestResult iTestResult
     */
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        saveInfoAndSendInform(iTestResult, false);
    }

    /**
     * 用例失败后执行
     *
     * @param iTestResult iTestResult
     */
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        saveInfoAndSendInform(iTestResult, true);
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("------------------------------------------开始执行------------------------------------------");
        System.out.println();
        Context.clearMethod();
        Context.clearClass();
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Context.clearMethod();
        Context.clearClass();
        System.out.println();
        System.out.println("------------------------------------------执行结束------------------------------------------");
    }

    /**
     * 测试类执行之前获取注解信息存入缓存
     *
     * @param testClass 测试类
     */
    @Override
    public void onBeforeClass(ITestClass testClass) {
        Class<?> realClass = testClass.getRealClass();
        Anno anno = new Anno();
        boolean collectAnnotation = realClass.isAnnotationPresent(Collector.class);
        if (collectAnnotation) {
            Collector annotation = realClass.getAnnotation(Collector.class);
            Class<? extends AbstractCollector> value = annotation.value();
            try {
                anno.setAbstractCollector(value.newInstance()).setSend(annotation.sendInform()).setSave(annotation.saveInfo());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ListenerException(e.getMessage());
            }
        }
        boolean containerAnnotation = realClass.isAnnotationPresent(Container.class);
        if (containerAnnotation) {
            Class<? extends IConfigContainer> value = realClass.getAnnotation(Container.class).value();
            try {
                IConfigContainer configContainer = value.newInstance();
                configContainer.setProperties(Property.parse());
                configContainer.init();
                anno.setConfigContainer(configContainer);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ListenerException(e.getMessage());
            }
        }
        // 将当前执行类与初始化数据放入内存
        Context.currentExecuteClass = realClass;
        Context.map.put(realClass, anno);
    }

    /**
     * 测试类执行之后清理缓存
     *
     * @param testClass 测试类
     */
    @Override
    public void onAfterClass(ITestClass testClass) {
        Context.clearClass();
    }

    /**
     * 保存数据&发送数据
     *
     * @param iTestResult 测试结果
     * @param sendInform  是否发送数据
     */
    private void saveInfoAndSendInform(ITestResult iTestResult, boolean sendInform) {
        Anno anno = Context.getAnno();
        if (anno != null) {
            AbstractCollector collector = Context.getCollector();
            if (collector != null) {
                if (anno.getSave()) {
                    collector.updateCase(info, iTestResult);
                }
                if (anno.getSend() && sendInform) {
                    collector.sendInform(info);
                }
            }
        }
        Context.clearMethod();
    }
}
