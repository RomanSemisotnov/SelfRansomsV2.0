package ru.marketboost.phone.services;

import io.vavr.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.marketboost.library.common.exceptions.MsModelNotFoundException;
import ru.marketboost.phone.hibernate.models.Phone;
import ru.marketboost.phone.hibernate.models.PhoneCode;
import ru.marketboost.phone.hibernate.repositories.PhoneCodeRepository;
import ru.marketboost.phone.hibernate.repositories.PhoneRepository;

@Service
public class PhoneCodeService {

    @Autowired
    private PhoneCodeRepository phoneCodeRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    public Phone setLast4DigitsToPhone(String myPhoneNumber, String marketPhoneNumber) throws MsModelNotFoundException {
        Phone phone = phoneRepository.findByNumber(myPhoneNumber)
                .orElseThrow(() -> new MsModelNotFoundException(Phone.class, Tuple.of("number", myPhoneNumber)));

        PhoneCode newPhoneCode = PhoneCode.builder()
                .phone(phone)
                .code(marketPhoneNumber.substring(marketPhoneNumber.length() - 4))
                .build();

        phoneCodeRepository.save(newPhoneCode);

        return phone;
    }

}
