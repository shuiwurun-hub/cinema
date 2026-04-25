package com.cinema.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getValueAsString();
        
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        // 尝试多种日期格式
        try {
            // 格式1: yyyy-MM-dd
            if (dateStr.length() == 10) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            }
            
            // 格式2: ISO 8601 (2027-07-31T16:00:00.000Z)
            if (dateStr.contains("T")) {
                // 提取日期部分
                String datePart = dateStr.substring(0, 10);
                return LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE);
            }
            
            // 格式3: 其他格式
            return LocalDate.parse(dateStr);
            
        } catch (DateTimeParseException e) {
            throw new IOException("无法解析日期: " + dateStr, e);
        }
    }
}
