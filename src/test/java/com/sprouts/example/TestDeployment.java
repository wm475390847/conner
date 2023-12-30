package com.sprouts.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sprouts.conner.annotation.Container;
import com.sprouts.conner.http.PartFile;
import com.sprouts.conner.response.ResponseLog;
import com.sprouts.conner.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试发布
 *
 * @author wangmin18
 * date  2023/12/12 17:07
 */
@Slf4j
@Container(value = LcapConfigContainer.class)
public class TestDeployment extends BaseCase {
    private static final String APP_ID = "aedbe008-968e-426e-8322-de7d128494e4";

    @Test
    public void test_development() {
        String str = FileUtil.readJsonFile("output.json");
        JSONObject object = JSONObject.parseObject(str);
        if (object == null) {
            return;
        }
        JSONArray array = object.getJSONArray("files");
        List<PartFile> files = array.stream()
                .map(JSONObject.class::cast)
                .map(e -> new PartFile().setKey("files")
                        .setContent(e.getString("content"))
                        .setFilename(e.getString("filename"))
                ).collect(Collectors.toList());

        ResponseLog<Response> deploymentsLog = DeploymentsRequest.builder()
                .files(files)
                .frontends(object.getString("frontends"))
                .logicAuthFlag(object.getString("logicAuthFlag"))
                .appId(object.getString("appId"))
                .replicas(object.getString("replicas"))
                .callbackLogicsName(object.getString("callbackLogicsName"))
                .reportIdList(object.getString("reportIdList"))
                .callLogicValidations(object.getString("callLogicValidations"))
                .logicPageResourceDtoList(object.getString("logicPageResourceDtoList"))
                .env(object.getString("env"))
                .roleList(object.getString("roleList"))
                .resourceList(object.getString("resourceList"))
                .noAuthApiPathList(object.getString("noAuthApiPathList"))
                .resourceDataList(object.getString("resourceDataList"))
                .roleResourceMappingList(object.getString("roleResourceMappingList"))
                .build()
                .execute();
        log.info(deploymentsLog.toString());
    }
}
