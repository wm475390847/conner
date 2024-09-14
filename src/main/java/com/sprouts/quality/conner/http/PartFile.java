package com.sprouts.quality.conner.http;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangmin18
 * @date 2023/12/24 17:04
 */
@Data
@Accessors(chain = true)
public class PartFile {

    private String key;
    private String filename;
    private String content;
}
