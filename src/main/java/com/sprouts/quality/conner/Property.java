package com.sprouts.quality.conner;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 属性解析类
 *
 * @author wangmin
 */
@Slf4j
public class Property {

    private Property() {
    }

    public static Properties parse() {
        return PropertyHolder.INSTANCE.properties;
    }

    public static Properties parse(String fileName) {
        PropertyHolder propertyHolder = new PropertyHolder(fileName);
        return propertyHolder.properties;
    }

    private static class PropertyHolder {
        private static final PropertyHolder INSTANCE = new PropertyHolder();
        private final Properties properties;

        private PropertyHolder() {
            String appEnv = System.getenv("APP_ENV");
            properties = appEnv == null ? loadProperties("env.properties") : loadProperties();
            log.info("配置文件加载完成");
            log.info("配置文件内容: {}", properties);
        }

        private PropertyHolder(String fileName) {
            log.info("加载配置文件");
            properties = loadProperties(fileName);
        }

        private static Properties loadProperties(String resourceName) {
            Properties properties = new Properties();
            try (InputStream resource = Property.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (resource != null) {
                    properties.load(new InputStreamReader(resource, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                throw new RuntimeException("加载配置文件失败", e);
            }
            return properties;
        }

        private static Properties loadProperties() {
            Properties properties = new Properties();
            String filePath = System.getProperty("user.dir");
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath + "/env.properties");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                properties.load(bufferedInputStream);
            } catch (IOException e) {
                log.error(e.toString());
            }
            return properties;
        }
    }
}
