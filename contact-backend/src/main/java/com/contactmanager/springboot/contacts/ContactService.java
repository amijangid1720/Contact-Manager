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

    public List<Contact> favoriteContacts(Integer userId) {
        return contactRepository.findByUserIdAndFavorite(userId, true);
    }

    public boolean checkEmailExists(String email) {

        Optional<Contact> contact = contactRepository.findByEmail(email);
        if (contact.isEmpty()) return false;
        else return true;
    }

    public boolean checkPhoneExists(String phoneno) {
        Optional<Contact> userInfo = contactRepository.findByPhoneno(phoneno);
        System.out.println("------------------------------------------------------");
        System.out.println(userInfo);
        if (userInfo.isEmpty()) return false;
        else return true;
    }

    public Contact toggleFavorite(Integer contactId, Boolean favorite) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setFavorite(favorite);
            return contactRepository.save(contact);
        } else {
            System.out.println("no contact found with id :" + contactId);
            return null;
        }
    }
}