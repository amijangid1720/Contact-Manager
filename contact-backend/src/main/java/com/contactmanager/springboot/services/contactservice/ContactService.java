package com.contactmanager.springboot.services.contactservice;

import com.contactmanager.springboot.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

public interface ContactService {
    public void addContact(Contact contact);
    public Page<Contact> searchContacts(String search, String filterTerm, Pageable pageable) ;
    public List<Contact> favoriteContacts(Integer userId);
    public boolean checkEmailExists(String email);
    public boolean checkPhoneExists(String phoneno);
    public Contact toggleFavorite(Integer contactId, Boolean favorite);
    public Contact removeFromFavorites(Integer contactId);
    public Contact getContactById(Integer contactId);
    public String convertContactsToCSV(List<Contact> contacts);
    public ResponseEntity<List<Contact>> getFamily(Integer userId);
    public ResponseEntity<List<Contact>> getFriends(Integer userId);
    public ResponseEntity<List<Contact>> getColleagues(Integer userId);

}
