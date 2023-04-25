package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.repository.ContactInfoRepository;
import com.unibuc.ismyblog.service.ContactInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    @Override
    public ContactInfo findById(Long contactInfoId) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(contactInfoId);
        if(contactInfoOptional.isEmpty()) {
            throw new NotFoundException("Contact info not found!!");
        }
        return contactInfoOptional.get();
    }

    @Override
    public void deleteContactInfo(Long contactInfoId) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(contactInfoId);
        if(contactInfoOptional.isEmpty()) {
            throw new NotFoundException("Contact info not found!!");
        }
        ContactInfo contactInfo = contactInfoOptional.get();
        User user = contactInfo.getUser();
        contactInfo.removeUser(user);
        contactInfoRepository.save(contactInfo);
        contactInfoRepository.deleteById(contactInfoId);
    }

}
