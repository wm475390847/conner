package com.sprouts.qaulity.conner.customer;

import com.sprouts.quality.conner.Context;
import com.sprouts.quality.conner.Property;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;

import java.util.Properties;

/**
 * 基础case类
 *
 * @author wangmin
 * @date 2021/12/28 7:15 下午
 */
@Slf4j
@Listeners(value = TestListener.class)
public abstract class AbstractCase {

    private static final String DEBUG = System.getProperty("DEBUG");
    protected static Properties properties = Property.parse();

    protected AbstractCase() {
        Context.DEBUG = Boolean.parseBoolean(DEBUG);
    }

    protected AbstractCase(boolean onDebug) {
        Context.DEBUG = onDebug;
        if (DEBUG != null) {
            Context.DEBUG = Boolean.parseBoolean(DEBUG);
        }
    }
}
