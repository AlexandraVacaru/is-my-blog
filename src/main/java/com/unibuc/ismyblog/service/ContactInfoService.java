package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.ContactInfo;

public interface ContactInfoService {
    ContactInfo save(ContactInfo contactInfo);
    ContactInfo findById(Long contactInfoId);
    void deleteContactInfo(Long contactInfoId);
}
