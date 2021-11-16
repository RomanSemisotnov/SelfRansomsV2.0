package ru.marketboost.library.common.http.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLastCodeRequest {

    private String number;
    private Long id;

}
