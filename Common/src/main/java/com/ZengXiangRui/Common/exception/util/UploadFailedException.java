package com.ZengXiangRui.Common.exception.util;

public class UploadFailedException extends RuntimeException {
    public UploadFailedException(String message) {
        super(message);
    }
    public UploadFailedException() {
        super();
    }
}
