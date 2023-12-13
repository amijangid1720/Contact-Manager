package com.contactmanager.springboot.services.contactservice;// DriveService.java

import com.contactmanager.springboot.entity.DriveQuickstart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;


@Service
public class DriveService {


    @Autowired
    DriveQuickstart driveQuickstart;

    public String uploadBasicFile(String csvData, String fileName,String email) {
        try {

            driveQuickstart.uploadData(csvData, "contacts.csv", email);

            return "File uploaded successfully!";
        } catch (GeneralSecurityException | IOException e) {
            // Handle the exceptions or log them
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage(); // or some appropriate error message
        }
    }

    public String downloadfile(String email,String destinationpath ) {
        try {

            driveQuickstart.downloadData(email,destinationpath);

            return "File downloaded successfully!";
        } catch (GeneralSecurityException | IOException e) {
            // Handle the exceptions or log them
            e.printStackTrace();
            return "Error downloading file: " + e.getMessage(); // or some appropriate error message
        }
    }

}
