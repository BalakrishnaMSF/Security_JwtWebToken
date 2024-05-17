package com.example.FinalTask3.controller;

import com.example.FinalTask3.model.Contact;
import com.example.FinalTask3.model.ContactDto;
import com.example.FinalTask3.model.PhoneNumber;
import com.example.FinalTask3.model.UserDetails;
import com.example.FinalTask3.repository.ContactRepository;
import com.example.FinalTask3.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/Contacts")
@Slf4j
public class ContactController {

    @Autowired
    private ContactService contactsService;


    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> saveContact(@Valid @RequestBody Contact contact) {

        log.info("Done" + contact);

        String errorMessage = null;
        if (contact.getPhonenums() != null) {
            for (PhoneNumber phoneNum : contact.getPhonenums()) {
                if (contactsService.availableContactByNumber(phoneNum.getMbNo())) {
                    errorMessage = "Constant.ERROR_MESSAGE";
                    break;
                }
            }
        }

        Contact savedContact = null;
        if (errorMessage == null) {
            savedContact = contactsService.saveContact(contact);
            log.info("Constant.CONTACT_SAVED_SUCCESSFULLY," + savedContact);
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("contact", savedContact);
        if (errorMessage != null) {
            responseMap.put("error", errorMessage);
        }

        return ResponseEntity.ok(responseMap);
    }




    private String validContact(Contact contact) {
        if (contact.getPhonenums() != null) {
            for (PhoneNumber phoneNum : contact.getPhonenums()) {
                if (contactsService.availableContactByNumber(phoneNum.getMbNo())) {
                    return "A contact with the same phone number already exists";
                }
            }
        }
        return null;
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contact>> getContactUser() {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("User found: {}", user);

        List<Contact> userContacts = contactsService.getContactsForUser(user);

        List<Contact> simplifiedContacts = userContacts.stream()
                .map(contact -> {
                    Contact simplifiedContact = new Contact();
                    simplifiedContact.setId(contact.getId());
                    simplifiedContact.setFirstName(contact.getFirstName());
                    simplifiedContact.setLastName(contact.getLastName());
                    simplifiedContact.setPhonenums(contact.getPhonenums());
                    simplifiedContact.setEmail(contact.getEmail());
                    simplifiedContact.setCreatedAt(contact.getCreatedAt());
                    simplifiedContact.setUpdatedAt(contact.getUpdatedAt());
                    return simplifiedContact;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(simplifiedContacts);
    }

    @GetMapping("/status/{PhNum}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> getContactStatus(@PathVariable String PhNum) {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("User found: {}", user);

        List<Contact> userContacts = contactsService.getContactsForUser(user);

        boolean contactExists = userContacts.stream()
                .anyMatch(contact -> contact.getPhonenums().stream()
                        .anyMatch(phoneNum -> phoneNum.getMbNo().equals(PhNum)));

        if (contactExists) {
            LocalDateTime latestUpdateTime = userContacts.stream()
                    .filter(contact -> contact.getPhonenums().stream()
                            .anyMatch(phoneNum -> phoneNum.getMbNo().equals(PhNum)))
                    .map(Contact::getUpdatedAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            if (latestUpdateTime != null && !latestUpdateTime.isEqual(userContacts.get(0).getCreatedAt())) {
                return ResponseEntity.ok("Contact was last updated at: " + latestUpdateTime);
            } else {
                return ResponseEntity.ok("Contact was not updated since creation");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @Valid @RequestBody Contact updatedContact) {
        log.info("Updating contact with ID: {}. New data: {}", contactId, updatedContact);

        Contact updatedContactResult = contactsService.updateContact(contactId, updatedContact);

        if (updatedContactResult != null) {
            return ResponseEntity.ok(updatedContactResult);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> allContacts = contactsService.getAllContacts();
        log.info("Returning {} contacts", allContacts.size());
        return ResponseEntity.ok(allContacts);
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ContactDto>> searchContacts(@RequestParam("term") String searchTerm) {
        try {
            List<ContactDto> matchingContacts = contactsService.searchContacts(searchTerm);
            return ResponseEntity.ok(matchingContacts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contact>> listContacts() {
        List<Contact> contacts = contactsService.listContacts();
        return ResponseEntity.ok(contacts);
    }
}

