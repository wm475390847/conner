package com.sprouts.conner;

import com.sprouts.conner.listener.ExecuteListener;
import com.sprouts.conner.utils.Property;
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
@Listeners(value = ExecuteListener.class)
public abstract class AbstractCase {

    private static final String DEBUG = System.getProperty("DEBUG");
    protected static Properties properties = Property.parse();

    protected AbstractCase() {
        Context.debug = Boolean.parseBoolean(DEBUG);
    }

    protected AbstractCase(boolean onDebug) {
        Context.debug = onDebug;
        if (DEBUG != null) {
            Context.debug = Boolean.parseBoolean(DEBUG);
        }
    }
}
