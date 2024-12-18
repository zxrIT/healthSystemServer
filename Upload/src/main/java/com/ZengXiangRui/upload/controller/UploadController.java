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

    @PostMapping("/csv/{username}/{userid}/{notesOnBills}")
    public String uploadCsv(@RequestParam("file") MultipartFile uploadFile,
                            @PathVariable String username,
                            @PathVariable String userid,
                            @PathVariable String notesOnBills) {
        return uploadService.uploadCsv(uploadFile, username, userid, notesOnBills);
    }
}
