package com.sprouts.conner;

import com.sprouts.conner.config.IConfigContainer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 注解管理
 *
 * @author wangmin
 * @date 2022/8/25 14:06
 */
@Data
@Accessors(chain = true)
public class Anno {

    /**
     * 配置容器
     */
    IConfigContainer configContainer;

    /**
     * 收集器
     */
    AbstractCollector abstractCollector;

    /**
     * 是否发送通知
     */
    Boolean send;

    /**
     * 是否保存用例数据
     */
    Boolean save;

    @Override
    public String toString() {
        return "Harbor{" +
                "configContainer=" + configContainer +
                ", abstractCollector=" + abstractCollector +
                ", send=" + send +
                ", save=" + save +
                '}';
    }
}
