package com.sprouts.qaulity.conner.cases;

import com.sprouts.qaulity.conner.api.DeploymentsRequest;
import com.sprouts.qaulity.conner.customer.BaseCase;
import com.sprouts.qaulity.conner.customer.TestConfigContainer;
import com.sprouts.quality.conner.annotation.Container;
import com.sprouts.quality.conner.response.ResponseLog;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.testng.annotations.Test;

/**
 * 测试发布
 *
 * @author wangmin18
 * @date 2023/12/12 17:07
 */
@Slf4j
@Container(value = TestConfigContainer.class)
public class TestDeployment extends BaseCase {

    @Test
    public void test_development() {
        ResponseLog<Response> deploymentsLog = DeploymentsRequest.builder().build().execute();
        log.info(deploymentsLog.toString());
    }
}