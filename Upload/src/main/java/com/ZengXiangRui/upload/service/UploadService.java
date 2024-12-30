package com.ZengXiangRui.upload.service;

import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    String uploadCsvAli(MultipartFile uploadFile, String username, String userid, String notesOnBills);

    String uploadCsvWeichat(MultipartFile uploadFile, String username, String userid, String notesOnBills);
}
