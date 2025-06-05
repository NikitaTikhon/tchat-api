package com.moro.tchat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime date;

    private Integer httpStatus;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String details;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;

}
