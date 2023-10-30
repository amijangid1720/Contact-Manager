package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.services.UserService;
import com.contactmanager.springboot.security.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;
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


    //id of the contact who we want to delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>  DeleteContact(@PathVariable Integer id, Authentication authentication){
        System.out.println(authentication.getName());
        contactRepository.deleteById(id);
        return ResponseEntity.ok("deletd" + id);
    }
    @GetMapping("/findAll")
    public  List<Contact> findAllContacts(Authentication authentication) throws Exception
    {
        User user =userService.loadUserByEmail(authentication.getName());
        System.out.println(user);
        List<Contact> contactList=contactRepository.findByUserId(user.getId());
        System.out.println(user);
        return contactList;

    }


    //id of the contact whose we want to update
    @PutMapping("/update/{id}")
    public  Contact updateContact(@RequestBody ContactRequest contactRequest,@PathVariable Integer id, Authentication authentication)throws Exception{

        User loggedInUser = userService.loadUserByEmail(authentication.getName());
        Contact contact = contactRepository.getById(id);
        contact.setFirstname(contactRequest.getFirstname());
        contact.setLastname(contactRequest.getLastname());
        contact.setWork(contactRequest.getWork());
        contact.setGender(contactRequest.getGender());
        contact.setEmail(contactRequest.getEmail());
        contact.setDescription(contactRequest.getDescription());
        contact.setPhoneno(contactRequest.getPhoneno());

        // Set the logged-in user as the owner of the contact
        contact.setUser(loggedInUser);
        contactRepository.save(contact);
        return contact;
    }

}
