package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.Entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;
    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    public List<Contact> searchContacts(String search) {
        // Implement your search logic here, e.g., using a JPA repository
        return contactRepository.searchContacts(search);
    }



    public boolean checkEmailExists(String email) {

        Optional<Contact> contact=contactRepository.findByEmail(email);
        if(contact.isEmpty()) return false;
        else return true;
    }

    public boolean checkPhoneExists(String phoneno) {
        Optional<Contact> userInfo=contactRepository.findByPhoneno(phoneno);
        System.out.println("------------------------------------------------------");
        System.out.println(userInfo);
        if(userInfo.isEmpty()) return false;
        else return true;
    }

}
