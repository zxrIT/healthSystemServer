package com.ZengXiangRui.Elasticsearch.content;

public class ElasticsearchMappingContent {
    public static String createMappingContent = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"transactionClassification\":{\n" +
            "        \"type\": \"text\",\n" +
            "         \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"counterparty\":{\n" +
            "        \"type\": \"text\", \n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"productDescription\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\":false\n" +
            "      },\n" +
            "      \"directionOfTrade\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"amountOfTransaction\":{\n" +
            "        \"type\": \"float\"\n" +
            "      },\n" +
            "      \"modeOfTransaction\":{\n" +
            "        \"type\": \"binary\",\n" +
            "        \"index\":false\n" +
            "      },\n" +
            "      \"transactionStatus\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\":false\n" +
            "      },\n" +
            "      \"tradeOrderNumber\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"remarks\":{\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"tradingHours\":{\n" +
            "        \"type\": \"date\",\n" +
            "        \"format\": \"MMM d, yyyy, HH:mm:ss a||MMM d, yyyy, hh:mm:ss a||MMM dd, yyyy, HH:mm:ss a||MMM dd, yyyy, hh:mm:ss a||epoch_millis||strict_date_optional_time||yyyy-MM-dd HH:mm:ss\"\n" +
            "      },\n" +
            "      \"userId\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}