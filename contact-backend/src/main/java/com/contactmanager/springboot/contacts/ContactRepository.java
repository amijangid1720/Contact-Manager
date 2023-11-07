package com.contactmanager.springboot.contacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
//    List<Contact> findByUserId(Integer id);

Page<Contact> findByUserId(Integer id, Pageable pageable);

}

