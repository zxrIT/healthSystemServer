package com.ZengXiangRui.processingBillCSV.csvProcessor;

import com.ZengXiangRui.Common.Utils.Encryption;
import com.ZengXiangRui.Common.exception.util.CSVProcessingException;
import com.ZengXiangRui.processingBillCSV.entity.CSVLineObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.*;

public class CSVProcessor {

    private static final int titleLine = 25;

    public static List<CSVLineObject> csvHandlerAli(String filePath) throws CSVProcessingException {
        List<CSVLineObject> csvLineObjectList = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = titleLine; i < sheet.getLastRowNum(); i++) {
                CSVLineObject csvLineObject = new CSVLineObject();
                Row rowNow = sheet.getRow(i);
                csvLineObject.setTradingHours(
                        Date.from(rowNow.getCell(0).getLocalDateTimeCellValue()
                                .toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
                );
                csvLineObject.setTransactionClassification(rowNow.getCell(1).getStringCellValue());
                csvLineObject.setCounterparty(rowNow.getCell(2).getStringCellValue());
                csvLineObject.setProductDescription(rowNow.getCell(4).getStringCellValue());
                csvLineObject.setDirectionOfTrade(
                        Objects.equals(rowNow.getCell(5).getStringCellValue(), "不计收支")
                                || Objects.equals(rowNow.getCell(5).getStringCellValue(), "收入") ?
                                0 : 1
                );
                csvLineObject.setAmountOfTransaction(rowNow.getCell(6).getNumericCellValue());
                csvLineObject.setModeOfTransaction(
                        rowNow.getCell(7) == null ? "" : rowNow.getCell(7).getStringCellValue()
                );
                csvLineObject.setTransactionStatus(rowNow.getCell(8).getStringCellValue());
                csvLineObject.setTradeOrderNumber(rowNow.getCell(9).getStringCellValue());
                csvLineObject.setRemarks(
                        rowNow.getCell(11) == null ? "" : rowNow.getCell(7).getStringCellValue()
                );
                csvLineObject.setId(Encryption.encryptToMd5(csvLineObject.getTradeOrderNumber()));
                csvLineObjectList.add(csvLineObject);
            }
            inputStream.close();
            return csvLineObjectList;
        } catch (Exception exception) {
            throw new CSVProcessingException(exception.getMessage());
        }
    }
}
