package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.Entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    public Page<Contact> searchContacts(String search , String filterTerm, Pageable pageable) {
        try {
            // Implement your search logic using a JPA repository with pagination
            return contactRepository.searchContacts(search,filterTerm, pageable);
        } catch (Exception e) {
            // Log the exception or handle it according to your requirements
            throw new RuntimeException("Error occurred during contact search", e);
        }
    }

    //get all fav
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

    //add to fav
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

    //remove from fav
    public Contact removeFromFavorites(Integer contactId) {
        Contact contact = getContactById(contactId);

        if (contact == null) {
            throw new NoSuchElementException("Contact not found");
        }

        contact.setFavorite(false);
        contactRepository.save(contact);

        return contact;
    }


    public Contact getContactById(Integer contactId) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        return optionalContact.orElse(null);
    }
}