package com.contactmanager.springboot.contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
