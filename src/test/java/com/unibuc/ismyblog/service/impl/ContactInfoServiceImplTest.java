package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.model.User;
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

    @Test
    public void givenAValidContactInfoSavesAndReturnsCi() {
        ContactInfo contactInfo = TestUtils.validContactInfo();

        when(contactInfoRepository.save(any())).thenReturn(contactInfo);

        ContactInfo savedContactInfo = sut.save(contactInfo);

        assertEquals(contactInfo, savedContactInfo);
    }

    @Test()
    public void givenNonExistentIdThrowsNotFound() {
        when(contactInfoRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> sut.findById(1L));

        assertNotNull(result);
        assertEquals("Contact info not found!!", result.getMessage());
    }

}