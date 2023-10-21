package com.example.springbootreporestapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RepoAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public RepoAPIException(HttpStatus status, String message){
        super(message);
        this.status = status;
        this.message = message;
    }

}
