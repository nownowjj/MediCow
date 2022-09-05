package com.medicow.service;

import com.medicow.model.constant.ContactStatus;
import com.medicow.model.dto.ContactFormDto;
import com.medicow.model.dto.ContactSearchDto;
import com.medicow.model.entity.Contact;
import com.medicow.model.entity.Hospital;
import com.medicow.repository.inter.ContactRepository;
import com.medicow.repository.inter.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public Page<Contact> getContactPage(ContactSearchDto contactSearchDto, Pageable pageable){
        // 상품 검색 조건을 이용하여 페이징 객체를 반환합니다.
        return contactRepository.getMainContactPage(contactSearchDto, pageable);
    }

    public Page<ContactFormDto> getContactPage(ContactSearchDto contactSearchDto,Long hosNo, Pageable pageable){
        List<Contact> contacts =null;
        Hospital hospital = hospitalRepository.findByHosNo(hosNo);
        Long totalCount = null;
        if(contactSearchDto.getSearchBy()==null || contactSearchDto.getSearchBy().equals("")){
            System.out.println("아무것도 검색이 안됨");
            contacts = contactRepository.findContactAllByHosNo(hospital,pageable);
            totalCount = contactRepository.countByHosNo(hospital);

        }else{
            if (contactSearchDto.getSearchBy().equals("subject")) {
                System.out.println("제목으로 검색함");
                contacts = contactRepository.findContactBySubject(contactSearchDto.getSearchQuery(),hospital,pageable);
                totalCount = contactRepository.countBySubject(contactSearchDto.getSearchBy(), hospital);
            }
            else if (contactSearchDto.getSearchBy().equals("content")) {
                System.out.println("내용으로 검색함");
                contacts = contactRepository.findContactByContent(contactSearchDto.getSearchQuery(),hospital,pageable);
                totalCount = contactRepository.countByContent(contactSearchDto.getSearchBy(), hospital);
            }else if (contactSearchDto.getSearchBy().equals("contactStatus")) {
                System.out.println("답변여부로 검색함");
                if (contactSearchDto.getSearchQuery().equals(ContactStatus.YES.toString())) {
                    contacts = contactRepository.findContactByAnswer(ContactStatus.YES,hospital,pageable);
                    totalCount = contactRepository.countByAsnwer(ContactStatus.YES, hospital);
                }else if (contactSearchDto.getSearchQuery().equals(ContactStatus.NO.toString())) {
                    contacts = contactRepository.findContactByAnswer(ContactStatus.NO,hospital,pageable);
                    totalCount = contactRepository.countByAsnwer(ContactStatus.NO, hospital);
                }

            }else{
                System.out.println("검색 에러 뭔가 이상함");
            }
        }
        List<ContactFormDto> contactFormDtos = new ArrayList<ContactFormDto>();
        if(contacts!=null){
            for (Contact contact : contacts) {
                ContactFormDto contactFormDto = new ContactFormDto();
                contactFormDto.setContactStatus(contact.getContactStatus());
                contactFormDto.setContent(contact.getContent());
                contactFormDto.setHospital(contact.getHospital());
                contactFormDto.setId(contact.getId());
                contactFormDto.setRegTime(contact.getRegTime());
                contactFormDto.setSubject(contact.getSubject());
                //contactFormDto.setAdminContent(contact.getAdminContent());
                contactFormDtos.add(contactFormDto);


            }
        }
        // 상품 검색 조건을 이용하여 페이징 객체를 반환합니다.
        return new PageImpl<ContactFormDto>(contactFormDtos, pageable,totalCount);
    }

    public Contact contactForm(ContactFormDto contactFormDto) {
        System.out.println("서비스로 넘어옴");
        Contact contact = contactFormDto.createContact();
        System.out.println("매핑하고 왔움");
        contactRepository.save(contact);
        return contact;
    }
    public ContactFormDto findContact(Long id){
        Contact contact = contactRepository.findContact(id);
        ContactFormDto contactFormDto = new ContactFormDto();
        contactFormDto.setContactStatus(contact.getContactStatus());
        contactFormDto.setContent(contact.getContent());
        contactFormDto.setHospital(contact.getHospital());
        contactFormDto.setId(contact.getId());
        contactFormDto.setRegTime(contact.getRegTime());
        contactFormDto.setSubject(contact.getSubject());
        contactFormDto.setAdminContent(contact.getAdminContent());
        return contactFormDto;
    }

    public ContactFormDto contactUpdate(Long id,String subject, String content){
        Integer okUpdate = contactRepository.contactUpdate(id,subject, content);
        if (okUpdate == 999) {
            System.out.println("업뎃 안됨");
        }
        Contact contact = contactRepository.findContact(id);
        ContactFormDto contactFormDto = new ContactFormDto();
        contactFormDto.setContactStatus(contact.getContactStatus());
        contactFormDto.setContent(contact.getContent());
        contactFormDto.setHospital(contact.getHospital());
        contactFormDto.setId(contact.getId());
        contactFormDto.setRegTime(contact.getRegTime());
        contactFormDto.setSubject(contact.getSubject());
        return contactFormDto;
    }

    @Transactional
    public Long updateContact(ContactFormDto contactFormDto) throws Exception{
        Contact contact = contactRepository.findById(contactFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        contact.updateContact(contactFormDto); // 화면에서 넘어온 dto를 이용하여 contact Entity에게 전달합니다.

        return contact.getId(); // 수정된 상품의 id를 반환합니다.
    }

    public void contactDelete(Long id){
        Integer okUpdate = contactRepository.contactDelete(id);
        if (okUpdate == 999) {
            System.out.println("삭제 안됨");
        }
    }

    public List<ContactFormDto> findTop3ByHospitalOrderByDesc(Hospital hospital) {
        List<Contact> contacts = contactRepository.findTop3ByHospitalOrderByRegTimeDesc(hospital);
        System.out.println(contacts.size());
        List<ContactFormDto> contactFormDtos = new ArrayList<ContactFormDto>();
        for (Contact contact : contacts) {
            ContactFormDto contactFormDto = new ContactFormDto();
            contactFormDto.setContactStatus(contact.getContactStatus());
            contactFormDto.setContent(contact.getContent());
            contactFormDto.setHospital(contact.getHospital());
            contactFormDto.setId(contact.getId());
            contactFormDto.setRegTime(contact.getRegTime());
            contactFormDto.setSubject(contact.getSubject());
            contactFormDtos.add(contactFormDto);
        }
        return contactFormDtos;
    }

}
