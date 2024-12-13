package com.ZengXiangRui.BookKeepingProvider.controller;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@SuppressWarnings("all")
@RequestMapping("/book/keeping")
public class BookKeepingController {
    @Autowired
    BookKeepingService bookKeepingService;

    @GetMapping("/getBookKeeping/{page}/{quantity}")
    public String getBookKeeping(@PathVariable int page, @PathVariable int quantity) {
        return bookKeepingService.getBookKeeping(page, quantity);
    }

    @PostMapping("/createBookKeeping")
    public String createBookKeeping(@RequestBody BookKeepingBill bookKeepingBill) {
        return bookKeepingService.createBookKeeping(bookKeepingBill);
    }

    @PutMapping("/updateBookKeeping")
    public String updateBookKeeping(@RequestBody BookKeepingBill bookKeepingBill) {
        return bookKeepingService.updateBookKeeping(bookKeepingBill);
    }
}
