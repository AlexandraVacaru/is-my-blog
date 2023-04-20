package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.repository.ContactInfoRepository;
import com.unibuc.ismyblog.service.ContactInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

}
