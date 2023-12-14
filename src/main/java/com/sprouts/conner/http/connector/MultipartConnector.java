package com.sprouts.conner.http.connector;

import com.sprouts.conner.http.FileContentTypeEnum;
import com.sprouts.conner.exception.ConnerException;
import com.sprouts.conner.http.Api;
import com.sprouts.conner.utils.FileUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2022/6/11 09:43
 */
public class MultipartConnector extends AbstractConnector {
    @Override
    protected void buildRequest(Request.Builder builder, Api api) {
        MediaType mediaType = MediaType.parse(api.getContentType());
        Optional.ofNullable(mediaType).orElseThrow(() -> new ConnerException("mediaType is null"));
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(mediaType);
        api.getPartParams().forEach(multipartBuilder::addFormDataPart);
        Map<String, String> partFiles = api.getPartFiles();
        if (!partFiles.isEmpty()) {
            partFiles.forEach((key, value) -> {
                String fileName = FileUtil.getLastName(value);
                byte[] fileBytes = FileUtil.getFileBytes(value);
                Optional.ofNullable(fileName).orElseThrow(() -> new ConnerException("fileName is empty"));
                RequestBody requestBody = RequestBody.create(MediaType.parse(
                        FileContentTypeEnum.findByFileName(fileName).getContentType()), fileBytes);
                multipartBuilder.addFormDataPart(key, fileName, requestBody);
            });
        }
        builder.post(multipartBuilder.build());
    }
}
