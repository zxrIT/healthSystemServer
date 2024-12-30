package com.ZengXiangRui.upload.controller;

import com.ZengXiangRui.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@SuppressWarnings("all")
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/csv/ali/{username}/{userid}/{notesOnBills}")
    public String uploadCsvAli(@RequestParam("file") MultipartFile uploadFile,
                               @PathVariable String username,
                               @PathVariable String userid,
                               @PathVariable String notesOnBills) {
        return uploadService.uploadCsvAli(uploadFile, username, userid, notesOnBills);
    }

    @PostMapping("/csv/weichat/{username}/{userid}/{notesOnBills}")
    public String uploadCsvWeiChat(@RequestParam("file") MultipartFile uploadFile,
                                   @PathVariable String username,
                                   @PathVariable String userid,
                                   @PathVariable String notesOnBills) {
        return uploadService.uploadCsvWeichat(uploadFile, username, userid, notesOnBills);
    }
}
