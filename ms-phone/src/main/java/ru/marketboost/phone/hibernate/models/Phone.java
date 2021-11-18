package ru.marketboost.phone.hibernate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Optional;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "phones")
@EqualsAndHashCode(callSuper = true)
public class Phone extends BaseEntity {

    @Column(name = "phone_number")
    private String number;

    @OneToMany(mappedBy = "phone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneCode> phoneCodes;

    public Optional<PhoneCode> getLastCode() {
        if (phoneCodes == null) {
            throw new IllegalStateException(String.format(
                    "Phone_codes in phone with id %s is null", id
            ));
        }

        if (phoneCodes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(phoneCodes.get(phoneCodes.size() - 1));
    }

}
