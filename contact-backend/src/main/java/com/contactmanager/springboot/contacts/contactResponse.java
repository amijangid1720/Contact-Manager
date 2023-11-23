package com.contactmanager.springboot.contacts;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class contactResponse {
    private List<Contact> contacts;
    private long totalContacts;
}
