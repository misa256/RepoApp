package com.example.springbootreporestapi.payload;

import com.example.springbootreporestapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ReportDto {
    private long id;
    private String place;
    private String date;
    private String title;
    private String text;
    private String userName;
    private String userEmail;
}
