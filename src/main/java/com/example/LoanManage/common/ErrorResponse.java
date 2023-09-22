package com.example.LoanManage.common;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {
    private String message;
    private List<String> details;

    public ErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(String message, String detail) {
        this.message = message;
        this.details = Collections.singletonList(detail);
    }
}
