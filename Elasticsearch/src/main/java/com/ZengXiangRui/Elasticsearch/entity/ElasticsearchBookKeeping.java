package com.ZengXiangRui.Elasticsearch.entity;

import com.ZengXiangRui.Common.Entity.Bill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(indexName = "bill")
public class ElasticsearchBookKeeping extends Bill {

    @Field(type = FieldType.Double, store = true)
    private double amountOfTransaction;

    @Field(type = FieldType.Text, store = true, index = false)
    private String counterparty;

    @Field(type = FieldType.Text, store = true, index = false)
    private int directionOfTrade;

    @Field(type = FieldType.Text, store = true, index = false)
    private String modeOfTransaction;

    @Field(type = FieldType.Text, store = true, index = false)
    private String productDescription;

    @Field(type = FieldType.Text, store = true, index = false)
    private String remarks;

    @Field(type = FieldType.Text, store = true)
    private String tradeOrderNumber;

    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradingHours;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word")
    private String transactionClassification;

    @Field(type = FieldType.Keyword, store = true)
    private String userId;
}
