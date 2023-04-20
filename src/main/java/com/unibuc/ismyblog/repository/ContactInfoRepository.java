package com.unibuc.ismyblog.repository;

import com.unibuc.ismyblog.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
