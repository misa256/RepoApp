package com.example.springbootreporestapi.payload;

import lombok.Data;

@Data
public class ReportDto {
    private long id;
    private String place;
    private String date;
    private String title;
    private String text;
}
