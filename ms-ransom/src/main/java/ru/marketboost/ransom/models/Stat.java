package ru.marketboost.ransom.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    private String name;
    private Integer wbCountGoods;
    private Boolean isNeedToRecheck;
    private Integer yandexWordstatCount;
}
