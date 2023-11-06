package com.contactmanager.springboot.contacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByUserId(Integer id);

    @Query("SELECT c FROM Contact c WHERE c.firstname LIKE %:search% OR c.lastname LIKE %:search% OR c.email LIKE %:search% OR c.work LIKE %:search%")
    List<Contact> searchContacts(@Param("search") String search);

}
