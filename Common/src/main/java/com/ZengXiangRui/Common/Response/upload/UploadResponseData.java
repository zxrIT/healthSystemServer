package com.ZengXiangRui.Common.Response.upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponseData {
    public String uploadId;
    public String uploadMessage;
}
