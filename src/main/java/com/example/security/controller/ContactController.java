package com.example.security.controller;

import com.example.security.model.Contact;
import com.example.security.model.ContactDto;
import com.example.security.model.PhoneNumber;
import com.example.security.model.User;
import com.example.security.service.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Contacts")
@Slf4j
public class ContactController {


    private final ContactService contactsService;

    public ContactController(ContactService contactsService) {
        this.contactsService = contactsService;
    }


    @PostMapping("/save")
//    @PreAuthorize("hasRole('ROLE_USER')")
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

    @GetMapping("/get")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contact>> getContactUser() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

    @GetMapping("/status/{phoneNumber}")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> getContactStatus(@PathVariable String phoneNumber) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("User found: {}", user);

        List<Contact> userContacts = contactsService.getContactsForUser(user);

        boolean contactExists = userContacts.stream()
                .anyMatch(contact -> contact.getPhonenums().stream()
                        .anyMatch(phoneNum -> phoneNum.getMbNo().equals(phoneNumber)));

        if (contactExists) {
            LocalDateTime latestUpdateTime = userContacts.stream()
                    .filter(contact -> contact.getPhonenums().stream()
                            .anyMatch(phoneNum -> phoneNum.getMbNo().equals(phoneNumber)))
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
//    @PreAuthorize("hasRole('ROLE_USER')")
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
//    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> allContacts = contactsService.getAllContacts();
        log.info("Returning {} contacts", allContacts.size());
        return ResponseEntity.ok(allContacts);
    }


    @GetMapping("/search")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ContactDto>> searchContacts(@RequestParam("term") String searchTerm) {
        try {
            List<ContactDto> matchingContacts = contactsService.searchContacts(searchTerm);
            return ResponseEntity.ok(matchingContacts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/list")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contact>> listContacts() {
        List<Contact> contacts = contactsService.listContacts();
        return ResponseEntity.ok(contacts);
    }
}

