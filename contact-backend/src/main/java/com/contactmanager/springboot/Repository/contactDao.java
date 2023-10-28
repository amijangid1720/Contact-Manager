package com.contactmanager.springboot.Repository;

import com.contactmanager.springboot.Entity.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface contactDao extends JpaRepository<Contacts, Integer> {
}
