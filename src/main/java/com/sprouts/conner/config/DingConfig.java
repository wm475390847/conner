package com.sprouts.conner.config;

import lombok.Getter;
import com.sprouts.conner.AbstractMessage;

import java.util.Arrays;

/**
 * 钉钉配置
 *
 * @author wangmin
 * @date 2022/5/18 11:27
 */
@Getter
public class DingConfig extends AbstractConfig {

    private String webhook;
    private String keyword;
    private String[] phones;
    private boolean isAtAll;
    private AbstractMessage messageFormat;

    public DingConfig messageFormat(AbstractMessage messageFormat) {
        this.messageFormat = messageFormat;
        return this;
    }

    public DingConfig webhook(String webhook) {
        this.webhook = webhook;
        return this;
    }

    public DingConfig keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public DingConfig isAtAll(Boolean isAtAll) {
        this.isAtAll = isAtAll;
        return this;
    }

    public DingConfig phones(String... phones) {
        this.phones = phones.clone();
        return this;
    }

    @Override
    public String toString() {
        return "DingDingConfig{" +
                "webhook='" + webhook + '\'' +
                ", keyword='" + keyword + '\'' +
                ", phones=" + Arrays.toString(phones) +
                ", isAtAll=" + isAtAll +
                ", messageFormat=" + messageFormat +
                '}';
    }
}
