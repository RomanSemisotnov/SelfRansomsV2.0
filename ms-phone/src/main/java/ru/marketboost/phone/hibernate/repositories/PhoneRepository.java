package ru.marketboost.phone.hibernate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.marketboost.phone.hibernate.models.Phone;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByPhoneNumber(String phoneNumber);

}
