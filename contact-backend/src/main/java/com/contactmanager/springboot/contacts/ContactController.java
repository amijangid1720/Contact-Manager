package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.Entity.UserInfo;
import com.contactmanager.springboot.Entity.UserInfoRequest;
import com.contactmanager.springboot.Repository.UserInfoRepository;
import com.contactmanager.springboot.Entity.UserInfoRequest;
import com.contactmanager.springboot.Repository.UserInfoRepository;
import com.contactmanager.springboot.security.Repository.UserRepository;
import com.contactmanager.springboot.security.services.UserService;
import com.contactmanager.springboot.security.user.User;
//import com.contactmanager.springboot.security.user.UserSession;
import com.contactmanager.springboot.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    @Autowired
    UserInfoService userInfoService;


    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @PostMapping("/")
    public ResponseEntity<ContactRequest> addContacts(@RequestBody ContactRequest contactRequest, Authentication authentication)
   {
//        User loggedInUser = userSession.getCurrentUser();

//        // Get the currently authenticated user
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
//        contactService.addContact(contact);
        return ResponseEntity.ok(contactRequest);
    }



    //id of the contact who we want to delete
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String>  DeleteContact(@PathVariable Integer id, Authentication authentication){
//        System.out.println(authentication.getName());
//        contactRepository.deleteById(id);
//        return ResponseEntity.ok("deleted: " + id);
//    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteContact(@PathVariable Integer id, Authentication authentication) {
        System.out.println(authentication.getName());

        try {
            contactRepository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("deleted", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to delete contact");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/findAll")
    public Page<Contact> findAllContacts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Authentication authentication
    ) throws Exception {
        User user = userService.loadUserByEmail(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        return contactRepository.findByUserId(user.getId(), pageable);
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

//update details of user using his id
    @PutMapping("/updateuser/{id}")
    public  UserInfo updateUser(@RequestBody UserInfoRequest userInfoRequest, @PathVariable Integer id, Authentication authentication)throws Exception{

        User loggedInUser = userService.loadUserByEmail(authentication.getName());
        UserInfo userInfo = userInfoRepository.getById(id);
        userInfo.setFirstName(userInfoRequest.getFirstName());
        userInfo.setLastName(userInfoRequest.getLastName());
        userInfo.setGender(userInfoRequest.getGender());
        userInfo.setEmail(userInfoRequest.getEmail());
        userInfo.setAddress(userInfoRequest.getAddress());
        userInfo.setPhoneno(userInfoRequest.getPhoneno());

        // Set the logged-in user as the owner of the contact
        userInfo.setUser(loggedInUser);
        userInfoRepository.save(userInfo);
        return userInfo;
    }

    @GetMapping("/contactinfo/{id}")
    public ResponseEntity<Contact> getContactInfo(@PathVariable Integer id) {
        Contact contactInfo = contactRepository.getById(id);
        if (contactInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contactInfo);
    }

    //to get details of the user
    @GetMapping("/userinfo/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable Integer id){
        UserInfo userinfo = userInfoRepository.findByUserId(id);
        if(userinfo == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userinfo);
    }

    //updating details of the user
    @PutMapping("/updateUser/{id}")
    public  UserInfo updateUser(@RequestBody UserInfoRequest userInfoRequest, @PathVariable Integer id, Authentication authentication)throws Exception{

        User loggedInUser = userService.loadUserByEmail(authentication.getName());
        UserInfo userInfo = userInfoRepository.findByUserId(id);
        userInfo.setFirstName(userInfoRequest.getFirstName());
        userInfo.setLastName(userInfoRequest.getLastName());
        userInfo.setGender(userInfoRequest.getGender());
        userInfo.setEmail(userInfoRequest.getEmail());
        userInfo.setAddress(userInfoRequest.getAddress());
        userInfo.setPhoneno(userInfoRequest.getPhoneno());

        // Set the logged-in user as the owner of the contact
        userInfo.setUser(loggedInUser);
       userInfoRepository.save(userInfo);
        return userInfo;
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("Hiiii");
        String username = userDetails.getUsername();
        UserInfo userInfo = userInfoService.getUserInfo(username);
        System.out.println(userInfo);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search/{searchQuery}")
    public ResponseEntity<List<Contact>> searchContacts(@PathVariable String searchQuery) {
        try {
            List<Contact> matchingContacts = contactService.searchContacts(searchQuery);
            return ResponseEntity.ok(matchingContacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
