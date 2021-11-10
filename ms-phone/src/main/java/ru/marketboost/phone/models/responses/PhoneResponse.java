package ru.marketboost.phone.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.marketboost.phone.hibernate.models.Phone;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneResponse {
    private List<Phone> phones;
}
