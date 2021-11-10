package ru.marketboost.phone.hibernate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "phones")
public class Phone extends BaseEntity {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_code")
    private String lastCode;

}
