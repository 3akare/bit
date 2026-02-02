package com.bit.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // If the field is null, don't include in JSON output
public class DefaultApiResponse<T> {
    @Schema(description = "HTTP Status Code", example = "200")
    private int statusCode;
    @Schema(description = "Status Message", example = "Success")
    private String stateMessage;
    @Schema(description = "Response Data")
    private T data;
}
