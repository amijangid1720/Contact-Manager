package com.contactmanager.springboot.controller;

import com.contactmanager.springboot.entity.Contact;
import com.contactmanager.springboot.entity.UserInfo;
import com.contactmanager.springboot.dao.ContactRepository;
import com.contactmanager.springboot.dto.ApiResponse;
import com.contactmanager.springboot.dto.UserInfoRequest;
import com.contactmanager.springboot.dao.UserInfoRepository;
import com.contactmanager.springboot.dto.ContactRequest;
import com.contactmanager.springboot.mapper.ContactMapper;
import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.dto.DuplicateCheckRequest;
import com.contactmanager.springboot.security.dto.DuplicateCheckResponse;
import com.contactmanager.springboot.security.services.UserService;
import com.contactmanager.springboot.security.entity.User;
//import com.contactmanager.springboot.security.user.UserSession;
import com.contactmanager.springboot.services.contactservice.ContactService;
import com.contactmanager.springboot.services.imageUpload.CloudinaryImageServiceImpl;
import com.contactmanager.springboot.services.userservice.UserInfoServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("api/v1/contacts")
public class ContactController {

    @Autowired
    UserInfoServiceImpl userInfoService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private CloudinaryImageServiceImpl cloudinaryImageService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;
//    @PostMapping("/")
//    public ResponseEntity<ContactRequest> addContacts(@RequestBody ContactRequest contactRequest, Authentication authentication) {
//        User loggedInUser = userService.loadUserByEmail(authentication.getName());
//
//        // Use ModelMapper to map ContactRequest to Contact
//        Contact contact = modelMapper.map(contactRequest, Contact.class);
//
//        // Set additional properties or modify as needed
//        contact.setFavorite(false);
//        contact.setUser(loggedInUser);
//
//        // Save the contact
//        contactService.addContact(contact);
//
//        // Return ResponseEntity with the mapped ContactRequest (if needed)
//        return ResponseEntity.ok(contactRequest);
//    }
//}
@PostMapping("/")
public ResponseEntity<ContactRequest> addContacts(@RequestBody ContactRequest contactRequest, Authentication authentication) {
    User loggedInUser = userService.loadUserByEmail(authentication.getName());

    // Use ModelMapper to map ContactRequest to Contact
    Contact contact = ContactMapper.convertContactRequestToContact(contactRequest);


    // Set additional properties or modify as needed
    contact.setFavorite(false);
    contact.setUser(loggedInUser);

    // Save the contact
    contactService.addContact(contact);

    // Return ResponseEntity with the mapped ContactRequest (if needed)
    return ResponseEntity.ok(contactRequest);
}


    //id of the contact who we want to delete
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
public ResponseEntity<ContactRequest.contactResponse> findAllContacts(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        Authentication authentication
) throws Exception {
    User user = userService.loadUserByEmail(authentication.getName());

    Pageable pageable = PageRequest.of(page, size);
    Page<Contact> contactPage = contactRepository.findByUserId(user.getId(), pageable);

    ContactRequest.contactResponse response = new ContactRequest.contactResponse();
    response.setContacts(contactPage.getContent());
    response.setTotalContacts(contactPage.getTotalElements());

    return ResponseEntity.ok(response);
}





    //id of the contact whose we want to update
//    @PutMapping("/update/{id}")
//    public  Contact updateContact(@RequestBody ContactRequest contactRequest,@PathVariable Integer id, Authentication authentication)throws Exception{
//
//        User loggedInUser = userService.loadUserByEmail(authentication.getName());
//        Contact contact = contactRepository.getById(id);
//        contact.setFirstname(contactRequest.getFirstname());
//        contact.setLastname(contactRequest.getLastname());
//        contact.setWork(contactRequest.getWork());
//        contact.setGender(contactRequest.getGender());
//        contact.setEmail(contactRequest.getEmail());
//        contact.setDescription(contactRequest.getDescription());
//        contact.setPhoneno(contactRequest.getPhoneno());
//        contact.setIsFriend(contactRequest.getIsFriend());
//        contact.setIsColleague(contactRequest.getIsColleague());
//        contact.setIsFamily(contactRequest.getIsFamily());
//
//        // Set the logged-in user as the owner of the contact
//        contact.setUser(loggedInUser);
//        contactRepository.save(contact);
//        return contact;
//    }
    @PutMapping("/update/{id}")
    public Contact updateContact(@RequestBody ContactRequest contactRequest, @PathVariable Integer id, Authentication authentication) throws Exception {
        User loggedInUser = userService.loadUserByEmail(authentication.getName());

        // Fetch the existing contact by ID
        Contact existingContact = contactRepository.getById(id);

        // Use ModelMapper to map ContactRequest to existing Contact
        modelMapper.map(contactRequest, existingContact);


        // Set additional properties or modify as needed
        existingContact.setIsFriend(contactRequest.getIsFriend());
        existingContact.setIsColleague(contactRequest.getIsColleague());
        existingContact.setIsFamily(contactRequest.getIsFamily());
        existingContact.setUser(loggedInUser);

        // Save the updated contact
        contactRepository.save(existingContact);

        // Return the updated contact
        return existingContact;
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

    // Profile Update
//    @PutMapping("/updateUser/{id}")
//    public  UserInfo updateUser(@RequestBody UserInfoRequest userInfoRequest, @PathVariable Integer id, Authentication authentication)throws Exception{
//
//        User loggedInUser = userService.loadUserByEmail(authentication.getName());
//        UserInfo userInfo = userInfoRepository.findByUserId(id);
//        userInfo.setFirstName(userInfoRequest.getFirstName());
//        userInfo.setLastName(userInfoRequest.getLastName());
//        userInfo.setGender(userInfoRequest.getGender());
//        userInfo.setEmail(userInfoRequest.getEmail());
//        userInfo.setAddress(userInfoRequest.getAddress());
//        userInfo.setPhoneno(userInfoRequest.getPhoneno());
//        userInfo.setUser(loggedInUser);
//        userInfoRepository.save(userInfo);
//        return userInfo;
//    }
    @PutMapping("/updateUser/{id}")
    public UserInfo updateUser(@RequestBody UserInfoRequest userInfoRequest, @PathVariable Integer id, Authentication authentication) throws Exception {
        User loggedInUser = userService.loadUserByEmail(authentication.getName());

        // Fetch the existing UserInfo by user ID
        UserInfo existingUserInfo = userInfoRepository.findByUserId(id);

        // Use ModelMapper to map UserInfoRequest to existing UserInfo
        modelMapper.map(userInfoRequest, existingUserInfo);

        // Set additional properties or modify as needed
        existingUserInfo.setUser(loggedInUser);

        // Save the updated UserInfo
        userInfoRepository.save(existingUserInfo);

        // Return the updated UserInfo
        return existingUserInfo;
    }
    @PutMapping("updateDetailsFilled/{id}")
    public ResponseEntity<ApiResponse> userDetailsFilled(@PathVariable Integer id) {
        try {
            userInfoService.markDetailsAsFilled(id);
            return ResponseEntity.ok().body(new ApiResponse("Details marked as filled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update detailsFilled"));
        }
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
    @GetMapping("/search/{searchQuery}/{filterTerm}")
    public ResponseEntity<ContactRequest.contactResponse> searchContacts(
            @PathVariable String searchQuery,@PathVariable String filterTerm,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Contact> matchingContacts = contactService.searchContacts(searchQuery,filterTerm, pageable);

            ContactRequest.contactResponse response = new ContactRequest.contactResponse();
            response.setContacts(matchingContacts.getContent());
            response.setTotalContacts(matchingContacts.getTotalElements());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ContactRequest.contactResponse());
        }
    }


    //to get list of fav contacts
    @GetMapping("/favorite/{userid}")
    public ResponseEntity<List<Contact>> favoriteContacts(@PathVariable Integer userid) {
        try {
            List<Contact> favorite = contactService.favoriteContacts(userid);
            return ResponseEntity.ok(favorite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/checkDuplicateContact")
    public DuplicateCheckResponse checkDuplicateContact(@RequestBody DuplicateCheckRequest request) {
        boolean emailExists = contactService.checkEmailExists(request.getEmail());
        boolean phoneExists = contactService.checkPhoneExists(request.getPhoneno());

        return new DuplicateCheckResponse(emailExists, phoneExists);
    }


    // to make  a contact favorite
    @PatchMapping("/{contactId}/favorite")
    public ResponseEntity<Contact> toggleFavorite(@PathVariable Integer contactId, @RequestBody Boolean isFavorite) {
        try {
            Contact updatedContact = contactService.toggleFavorite(contactId, isFavorite);
            return ResponseEntity.ok(updatedContact);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //to remove a contact from favorite
    @PatchMapping("/{contactId}/unfavorite")
    public ResponseEntity<Contact> removeFromFavorites(@PathVariable Integer contactId) {
        try {
            Contact updatedContact = contactService.removeFromFavorites(contactId);
            return ResponseEntity.ok(updatedContact);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-profile/{userid}")
   public ResponseEntity<Map> uploadImage(
            @RequestParam("image") MultipartFile file,
            @PathVariable Integer userid
    )
    {
      Map data=this.cloudinaryImageService.upload(file,userid);
      userInfoService.updateUserProfilePicture(data);
      return new ResponseEntity<>(data,HttpStatus.OK);
    }
    @GetMapping("/family/{userId}")
    public ResponseEntity<List<Contact>> getFamily(@PathVariable Integer userId)
    {
        return contactService.getFamily(userId);
    }
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Contact>> getFriends(@PathVariable Integer userId)
    {
        return contactService.getFriends(userId);
    }

    @GetMapping("/colleagues/{userId}")
    public ResponseEntity<List<Contact>> getColleagues(@PathVariable Integer userId)
    {
        return contactService.getColleagues(userId);
    }

}


