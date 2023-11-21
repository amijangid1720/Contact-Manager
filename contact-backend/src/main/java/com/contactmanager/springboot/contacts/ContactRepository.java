package com.contactmanager.springboot.contacts;

import com.contactmanager.springboot.Entity.UserInfo;
import com.contactmanager.springboot.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
//    List<Contact> findByUserId(Integer id);

Page<Contact> findByUserId(Integer id, Pageable pageable);

    List<Contact> findByUserId(Integer id);

    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhoneno(String phoneno);

    @Query("SELECT c FROM Contact c WHERE c.firstname LIKE %:search% OR c.lastname LIKE %:search% OR c.email LIKE %:search% OR c.work LIKE %:search%")
    List<Contact> searchContacts(@Param("search") String search);

}

