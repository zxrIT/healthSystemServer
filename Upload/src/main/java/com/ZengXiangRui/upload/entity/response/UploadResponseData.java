package com.ZengXiangRui.upload.entity.response;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UploadResponseData extends com.ZengXiangRui.Common.Response.upload.UploadResponseData {
    public UploadResponseData(String uploadId, String uploadMessage) {
        super(uploadId, uploadMessage);
    }
}
