package com.example.springbootreporestapi.payload;

import lombok.Data;

@Data
public class InputReportDto {
    private String place;
    private String date;
    private String title;
    private String text;
    private Long userId;
}
