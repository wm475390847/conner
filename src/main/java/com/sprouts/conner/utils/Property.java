package com.sprouts.conner.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 属性解析类
 *
 * @author wangmin
 * @date 2021/12/23 11:53 下午
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
            log.info("加载配置文件");
            properties = loadProperties("env.properties");
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
    }
}