//package com.contactmanager.springboot.services;
//
//import com.contactmanager.springboot.contacts.Contact;
//import com.google.api.client.http.InputStreamContent;
//import com.google.api.services.drive.Drive;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Collections;
//import java.util.List;
//
//public class GoogleDriveService {
//
//
//    private DriveService driveService;
//    private static final String FOLDER_ID = "your_folder_id";
//
//    public String uploadContactsToDrive(List<Contact> contacts) throws IOException {
//        // Create a text file with contact information
//        String content = generateContactContent(contacts);
//        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
//
//        File fileMetadata = new File();
//        fileMetadata.setName("contacts.txt");
//        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));
//
//        Drive.Files.Create createRequest = driveService.files().create(fileMetadata, new InputStreamContent("text/plain", inputStream));
//        File uploadedFile = createRequest.execute();
//
//        return uploadedFile.getId();
//    }
//
//    private String generateContactContent(List<Contact> contacts) {
//        // Implement your logic to generate the content of the file
//        // For simplicity, concatenate contact information as plain text
//        StringBuilder contentBuilder = new StringBuilder();
//        contacts.forEach(contact -> {
//            contentBuilder.append("Name: ").append(contact.getFirstname()).append(", Email: ").append(contact.getEmail()).append("\n");
//        });
//        return contentBuilder.toString();
//    }
//
//}
