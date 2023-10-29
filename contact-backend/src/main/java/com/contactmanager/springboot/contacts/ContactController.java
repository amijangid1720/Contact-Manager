package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.security.services.UserService;
import com.contactmanager.springboot.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;
    @PostMapping("/add")
    public ResponseEntity<ContactRequest> addContacts(@RequestBody ContactRequest contactRequest, Authentication authentication)
    {

        // Get the currently authenticated user
        User loggedInUser = userService.loadUserByEmail(authentication.getName());

        // Create a Contact object and associate it with the logged-in user
        Contact contact = new Contact();
        contact.setFirstname(contactRequest.getFirstname());
        contact.setLastname(contactRequest.getLastname());
        contact.setWork(contactRequest.getWork());
        contact.setGender(contactRequest.getGender());
        contact.setEmail(contactRequest.getEmail());
        contact.setDescription(contactRequest.getDescription());
        contact.setPhoneno(contactRequest.getPhoneno());

        // Set the logged-in user as the owner of the contact
        contact.setUser(loggedInUser);

        // Save the contact
        contactService.addContact(contact);

        contactService.addContact(contact);
        return ResponseEntity.ok(contactRequest);
    }
}
