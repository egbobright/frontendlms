package com.lms.frontend.infrastructure;

import lombok.Data;

@Data
public class ApiBaseResponse<T> {
    private String responseMessage;
    private String responseCode;
    private T responseData;
}
