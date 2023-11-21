package com.contactmanager.springboot.contacts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class contactResponse {
    private List<Contact> contacts;
    private long totalContacts;
}
