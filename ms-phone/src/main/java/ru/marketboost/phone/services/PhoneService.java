package ru.marketboost.phone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.marketboost.phone.hibernate.models.Phone;
import ru.marketboost.phone.hibernate.repositories.PhoneRepository;

import java.util.List;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    public Phone storePhone(String phoneNumber) {
        return phoneRepository.save(Phone.builder().phoneNumber(phoneNumber).build());
    }

    public List<Phone> findAll() {
        return phoneRepository.findAll();
    }

    public Phone getById(long id) {
        return phoneRepository.getById(id);
    }

    public void deleteByIds(List<Long> ids) {
        phoneRepository.deleteAllById(ids);
    }

    public Phone setLast4DigitsToPhone(String myPhoneNumber, String marketPhoneNumber) {
        Phone phone = phoneRepository.findByPhoneNumber(myPhoneNumber).orElse(null);

        phone.setLastCode(marketPhoneNumber.substring(marketPhoneNumber.length() - 4));

        phoneRepository.save(phone);

        return phone;
    }

}
