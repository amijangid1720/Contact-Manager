package com.contactmanager.springboot.mapper;

import com.contactmanager.springboot.dto.ContactRequest;
import com.contactmanager.springboot.entity.Contact;

public class ContactMapper {


    public static ContactRequest convertContactToContactRequest(Contact contact){
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setFirstname(contact.getFirstname());
        contactRequest.setLastname(contact.getLastname());
        contactRequest.setWork(contact.getWork());
        contactRequest.setGender(contact.getGender());
        contactRequest.setEmail(contact.getEmail());
        contactRequest.setDescription(contact.getDescription());
        contactRequest.setFavourite(String.valueOf(contact.getFavorite()));
        contactRequest.setPhoneno(contact.getPhoneno());
        contactRequest.setIsFriend(contact.getIsFriend());
        contactRequest.setIsFamily(contact.getIsFamily());
        contactRequest.setIsColleague(contact.getIsColleague());
        return  contactRequest;
    }
    public static Contact convertContactRequestToContact(ContactRequest contactRequest)
    {
        Contact contact=new Contact();
        contact.setFirstname(contactRequest.getFirstname());
        contact.setLastname(contactRequest.getLastname());
        contact.setWork(contactRequest.getWork());
        contact.setGender(contactRequest.getGender());
        contact.setEmail(contactRequest.getEmail());
        contact.setDescription(contactRequest.getDescription());
        contact.setFavorite(Boolean.valueOf(contactRequest.getFavourite()));
        contact.setPhoneno(contactRequest.getPhoneno());
        contact.setIsFriend(contactRequest.getIsFriend());
        contact.setIsFamily(contactRequest.getIsFamily());
        contact.setIsColleague(contactRequest.getIsColleague());
        return contact;
    }
}
