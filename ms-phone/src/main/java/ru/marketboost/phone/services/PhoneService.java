package ru.marketboost.phone.services;

import io.vavr.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.marketboost.library.common.exceptions.MsAlreadyExistsException;
import ru.marketboost.phone.hibernate.models.Phone;
import ru.marketboost.phone.hibernate.repositories.PhoneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    public Phone storePhone(String phoneNumber) throws MsAlreadyExistsException {
        Optional<Phone> phone = phoneRepository.findByNumber(phoneNumber);

        if (phone.isPresent()) {
            throw new MsAlreadyExistsException(Phone.class, Tuple.of("number", phoneNumber));
        }

        return phoneRepository.save(Phone.builder().number(phoneNumber).build());
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

}
