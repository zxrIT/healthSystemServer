package com.ZengXiangRui.upload.service;

import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    String uploadCsv(MultipartFile uploadFile, String username, String userid, String notesOnBills);
}
