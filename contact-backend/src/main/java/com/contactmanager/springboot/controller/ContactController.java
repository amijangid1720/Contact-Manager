package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.dto.ApiResponse;
import com.contactmanager.springboot.dto.ContactRequest;
import com.contactmanager.springboot.entity.Contact;
import com.contactmanager.springboot.entity.UserInfo;
import com.contactmanager.springboot.dto.UserInfoRequest;
import com.contactmanager.springboot.dao.UserInfoRepository;
import com.contactmanager.springboot.dao.ContactRepository;
import com.contactmanager.springboot.mapper.ContactMapper;
import com.contactmanager.springboot.security.dao.UserRepository;
import com.contactmanager.springboot.security.dto.DuplicateCheckRequest;
import com.contactmanager.springboot.security.dto.DuplicateCheckResponse;
import com.contactmanager.springboot.security.services.UserService;
import com.contactmanager.springboot.security.entity.User;
import com.contactmanager.springboot.services.contactservice.DriveService;
import com.contactmanager.springboot.services.imageUpload.CloudinaryImageUploadService;
import com.contactmanager.springboot.services.userservice.UserInfoService;
import com.contactmanager.springboot.services.contactservice.ContactService;
import com.contactmanager.springboot.services.imageUpload.CloudinaryImageServiceImpl;
import com.contactmanager.springboot.services.userservice.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
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
    private CloudinaryImageUploadService cloudinaryImageService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;


    @Autowired
    ContactService contactService;

    @PostMapping("/")
    public ResponseEntity<ContactRequest> addContacts(@RequestBody ContactRequest contactRequest, Authentication authentication) {
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
        contact.setFavorite(false);
        contact.setIsFriend(contactRequest.getIsFriend());
        contact.setIsColleague(contactRequest.getIsColleague());
        contact.setIsFamily(contactRequest.getIsFamily());
        // Set the logged-in user as the owner of the contact
        contact.setUser(loggedInUser);
        // Save the contact
        contactService.addContact(contact);
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
        Contact contact = contactRepository.getById(id);
        contact.setFirstname(contactRequest.getFirstname());
        contact.setLastname(contactRequest.getLastname());
        contact.setWork(contactRequest.getWork());
        contact.setGender(contactRequest.getGender());
        contact.setEmail(contactRequest.getEmail());
        contact.setDescription(contactRequest.getDescription());
        contact.setPhoneno(contactRequest.getPhoneno());
        contact.setIsFriend(contactRequest.getIsFriend());
        contact.setIsColleague(contactRequest.getIsColleague());
        contact.setIsFamily(contactRequest.getIsFamily());

        // Set the logged-in user as the owner of the contact
        contact.setUser(loggedInUser);
        contactRepository.save(contact);
        return contact;
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
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable Integer id) {
        UserInfo userinfo = userInfoRepository.findByUserId(id);
        if (userinfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userinfo);
    }

    // Profile Update
    @PutMapping("/updateUser/{id}")
    public UserInfo updateUser(@RequestBody UserInfoRequest userInfoRequest, @PathVariable Integer id, Authentication authentication) throws Exception {
        User loggedInUser = userService.loadUserByEmail(authentication.getName());
        UserInfo userInfo = userInfoRepository.findByUserId(id);
        userInfo.setFirstName(userInfoRequest.getFirstName());
        userInfo.setLastName(userInfoRequest.getLastName());
        userInfo.setGender(userInfoRequest.getGender());
        userInfo.setEmail(userInfoRequest.getEmail());
        userInfo.setAddress(userInfoRequest.getAddress());
        userInfo.setPhoneno(userInfoRequest.getPhoneno());
        userInfo.setUser(loggedInUser);
        userInfoRepository.save(userInfo);
        return userInfo;
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{searchQuery}/{filterTerm}")
    public ResponseEntity<ContactRequest.contactResponse> searchContacts(
            @PathVariable String searchQuery, @PathVariable String filterTerm,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Contact> matchingContacts = contactService.searchContacts(searchQuery, filterTerm, pageable);

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
    ) {
        Map data = this.cloudinaryImageService.upload(file, userid);
        userInfoService.updateUserProfilePicture(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    //for backup of contacts
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadContacts(@RequestBody List<Contact> contacts, Authentication authentication) {
        try {
            User user = userService.loadUserByEmail(authentication.getName());
            String email = user.getEmail();
            Integer id  = user.getId();
            List<Contact> allcontacts = contactService.allContacts(id);
            // For simplicity, let's just print the contacts to the console
            System.out.println("Received contacts:");
            allcontacts.forEach(contact -> System.out.println("Name: " + contact.getFirstname() + ", Email: " + contact.getEmail() + ", Phoneno: " + contact.getPhoneno()));

            // to convert Contacts to csv filee.
            String csvData = contactService.convertContactsToCSV(allcontacts);
            System.out.println("2");

            driveService.uploadBasicFile(csvData, "contacts.csv", email);
            // Integrate with Google Drive API
//            contactService.uploadContactsToDrive(contacts);

            return ResponseEntity.ok().body(new ApiResponse("Contacts uploaded successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("Error uploading contacts"));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<ApiResponse> downloadContacts(Authentication authentication) {
        try {
            User user = userService.loadUserByEmail(authentication.getName());
            String email = user.getEmail();
            System.out.println("download contacts");

            String destinationFilePath = "/home/manisha/Downloads/file.csv";
            driveService.downloadfile(email, destinationFilePath);
            return ResponseEntity.ok().body(new ApiResponse("contacts downloaded successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse("error downloading contacts"));
        }

    }


    @GetMapping("/family/{userId}")
    public ResponseEntity<List<Contact>> getFamily(@PathVariable Integer userId) {
        return ResponseEntity.ok(contactRepository.findByUserIdAndIsFamily(userId, true));
    }

    @GetMapping("/friends/{userid}")
    public ResponseEntity<List<Contact>> getFriends(@PathVariable Integer userId) {
        return ResponseEntity.ok(contactRepository.findByUserIdAndIsFriend(userId, true));
    }

    @GetMapping("/colleagues/{userid}")
    public ResponseEntity<List<Contact>> getColleagues(@PathVariable Integer userId) {
        return ResponseEntity.ok(contactRepository.findByUserIdAndIsColleague(userId, true));
    }

    @GetMapping("/allContacts/{userId}")
    public ResponseEntity<List<Contact>> getAllContacts(@PathVariable Integer userId) {
        try {
            List<Contact> contacts = contactService.allContacts(userId);
            System.out.println(contacts);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }

    }
}
//@GetMapping("/favorite/{userid}")
//public ResponseEntity<List<Contact>> favoriteContacts(@PathVariable Integer userid) {
//    try {
//        List<Contact> favorite = contactService.favoriteContacts(userid);
//        return ResponseEntity.ok(favorite);
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//    }
//}