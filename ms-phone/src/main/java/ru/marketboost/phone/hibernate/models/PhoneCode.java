package ru.marketboost.phone.hibernate.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "phone_codes")
@EqualsAndHashCode(callSuper = true)
public class PhoneCode extends BaseEntity {

    @Column(name = "value")
    private String code;

    @Column(name = "phone_id")
    private String phoneId;

    @OneToMany
    private Phone phone;

}
