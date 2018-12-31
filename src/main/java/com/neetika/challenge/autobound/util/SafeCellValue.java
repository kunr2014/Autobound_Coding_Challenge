package com.neetika.challenge.autobound.util;

public class SafeCellValue {

    public static String get(String cellData) {
        if (cellData != null) {
            return cellData.replaceAll("\n", "");
        }
        return "N/A";
    }

}
