package com.sprouts.quality.conner;

import com.sprouts.quality.conner.annotation.Collector;
import com.sprouts.quality.conner.annotation.Container;
import com.sprouts.quality.conner.config.IConfigContainer;
import lombok.Builder;
import lombok.Getter;

/**
 * 注解管理类
 */
@Getter
@Builder
public class Anno {

    Collector caseCollector;

    Container caseContainer;

    IConfigContainer configContainer;

}
