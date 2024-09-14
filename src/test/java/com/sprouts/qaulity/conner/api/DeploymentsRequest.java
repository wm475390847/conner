package com.sprouts.qaulity.conner.api;

import com.sprouts.quality.conner.http.Api;
import com.sprouts.quality.conner.http.MethodEnum;
import com.sprouts.quality.conner.http.PartFile;
import com.sprouts.quality.conner.request.AbstractHttpRequest;
import lombok.Builder;

import java.util.List;

/**
 * 发布接口
 *
 * @author wangmin18
 * @date 2023/12/12 17:21
 */
@Builder
public class DeploymentsRequest extends AbstractHttpRequest {

    private String roleList;
    private String resourceList;
    private String resourceDataList;
    private String roleResourceMappingList;
    private String reportIdList;
    private String appId;
    private String env;
    private String logicPageResourceDtoList;
    private String logicAuthFlag;
    private String noAuthApiPathList;
    private String callLogicValidations;
    private String callbackLogicsName;
    private String frontends;
    private String replicas;
    private List<PartFile> files;

    @Override
    protected Api buildApi() {
        Api.Builder builder = new Api.Builder()
                .path("/api/v1/app/deployments")
                .method(MethodEnum.MULTIPART)
                .contentType("multipart/form-data")
                .formDataPart("roleList", roleList)
                .formDataPart("resourceList", resourceList)
                .formDataPart("resourceDataList", resourceDataList)
                .formDataPart("roleResourceMappingList", roleResourceMappingList)
                .formDataPart("reportIdList", reportIdList)
                .formDataPart("appId", appId)
                .formDataPart("env", env)
                .formDataPart("logicPageResourceDtoList", logicPageResourceDtoList)
                .formDataPart("logicAuthFlag", logicAuthFlag)
                .formDataPart("noAuthApiPathList", noAuthApiPathList)
                .formDataPart("callLogicValidations", callLogicValidations)
                .formDataPart("callbackLogicsName", callbackLogicsName)
                .formDataPart("frontends", frontends)
                .formDataPart("replicas", replicas);
        files.forEach(builder::filePart);
        return builder.build();
    }

    @Override
    protected Object buildBody() {
        return null;
    }
}