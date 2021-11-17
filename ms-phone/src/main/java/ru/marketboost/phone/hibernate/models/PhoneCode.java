package ru.marketboost.phone.hibernate.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "phone_codes")
@EqualsAndHashCode(callSuper = true)
public class PhoneCode extends BaseEntity {

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private Phone phone;

}
