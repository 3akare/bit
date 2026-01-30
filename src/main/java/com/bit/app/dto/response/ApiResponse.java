package com.bit.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // If the field is null, don't include in JSON output
public class ApiResponse<T> {
    private int statusCode;
    private String stateMessage;
    private T data;
}
