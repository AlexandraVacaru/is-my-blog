package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.repository.ContactInfoRepository;
import com.unibuc.ismyblog.utils.TestUtils;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactInfoServiceImplTest {

    @Mock
    ContactInfoRepository contactInfoRepository;

    @InjectMocks
    ContactInfoServiceImpl sut;

    @Test
    public void givenAValidIdFindReturnsContactWithGivenId() {
        ContactInfo contactInfo = TestUtils.validContactInfo();

        when(contactInfoRepository.findById(any())).thenReturn(Optional.ofNullable(contactInfo));

        ContactInfo newContactInfo = sut.findById(contactInfo.getContactInfoId());

        assertEquals(contactInfo, newContactInfo);
    }

}