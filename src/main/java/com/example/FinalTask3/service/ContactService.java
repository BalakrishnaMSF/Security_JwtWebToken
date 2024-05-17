package com.example.FinalTask3.service;


import com.example.FinalTask3.constants.StringConstants;
import com.example.FinalTask3.model.Contact;
import com.example.FinalTask3.model.ContactDto;
import com.example.FinalTask3.model.PhoneNumber;
import com.example.FinalTask3.model.UserDetails;
import com.example.FinalTask3.repository.ContactRepository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactRepository contactsRepository;

    public boolean availableContactByNumber(String phoneNumber) {
        return contactsRepository.existsByPhonenums_MbNo(phoneNumber);
    }
    public Contact updateContact(Long contactId, Contact updatedContact) {
        logger.info(StringConstants.UPDATE_CONTACT, contactId, updatedContact);
        Contact oldContact = contactsRepository.findById(contactId).orElse(null);

        if (oldContact != null) {
            oldContact.setFirstName(updatedContact.getFirstName());
            oldContact.setLastName(updatedContact.getLastName());
            oldContact.setEmail(updatedContact.getEmail());
            oldContact.setUpdatedAt(LocalDateTime.now());

            Contact updatedContactResult = contactsRepository.save(oldContact);
            logger.info(StringConstants.CONTACT_UPDATED, updatedContactResult);
            return updatedContactResult;
        } else {
            logger.warn(StringConstants.CONTACT_ID, contactId);
            return null;
        }
    }


    public Contact saveContact(Contact contact) {
        logger.info(StringConstants.SAVE, contact);
        return contactsRepository.save(contact);
    }

    public List<Contact> getContactsForUser(UserDetails user) {
        logger.info(StringConstants.GET, user);
        return contactsRepository.findByUserInfo(user);
    }

    public List<Contact> getAllContacts() {
        logger.info(StringConstants.GET_ALL);
        return contactsRepository.findAll();
    }

    public List<ContactDto> searchContacts(String searchTerm) {
        if (searchTerm.length() < 2) {
            throw new IllegalArgumentException("Search term must have at least 2 characters.");
        }

        List<Contact> matchingContacts = contactsRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm);

        // Convert Contact objects to ContactDTO objects
        List<ContactDto> result = new ArrayList<>();
        for (Contact contact : matchingContacts) {
            for (PhoneNumber phoneNumber : contact.getPhonenums()) {
                ContactDto dto = new ContactDto();
                dto.setFirstName(contact.getFirstName());
                dto.setLastName(contact.getLastName());
                dto.setPhoneNumber(phoneNumber.getMbNo());
                dto.setType(phoneNumber.getType());
                result.add(dto);
            }
        }
        return result;
    }


    @Cacheable(value = "contacts", key = "'allContacts'", unless = "#result == null")
    public List<Contact> listContacts() {
        return contactsRepository.findAll();
    }

}

