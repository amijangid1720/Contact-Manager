package com.contactmanager.springboot.services.contactservice;// DriveService.java

import com.contactmanager.springboot.Entity.DriveQuickstart;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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

    public String downloadfile(String id,String email,String destinationpath ) {
        try {

            driveQuickstart.downloadData(id, email,destinationpath);

            return "File downloaded successfully!";
        } catch (GeneralSecurityException | IOException e) {
            // Handle the exceptions or log them
            e.printStackTrace();
            return "Error downloading file: " + e.getMessage(); // or some appropriate error message
        }
    }

}
