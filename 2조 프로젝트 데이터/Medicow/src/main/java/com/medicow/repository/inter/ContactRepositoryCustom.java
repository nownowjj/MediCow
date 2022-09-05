package com.medicow.repository.inter;

import com.medicow.model.dto.ContactSearchDto;
import com.medicow.model.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContactRepositoryCustom {
    Page<Contact> getMainContactPage(ContactSearchDto contactSearchDto, Pageable pageable) ;
}
