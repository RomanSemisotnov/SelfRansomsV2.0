package ru.marketboost.library.common.exceptions;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String className;

    private String code;

    private String session;

    private String error;
}
