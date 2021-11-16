package ru.marketboost.library.common.http.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLastCodeResponse {

    private Long id;
    private String number;
    private String code;

}
