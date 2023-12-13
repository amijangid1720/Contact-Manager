package com.contactmanager.springboot.entity;

import com.contactmanager.springboot.services.userservice.UserInfoService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class DriveQuickstart {

    @Autowired
    UserInfoService UserInfoService;
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, String userEmail) throws IOException {
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        System.out.println("email" + userEmail);
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userEmail);
    }


    //    public void uploadData(String csvData, String fileName, String userEmail) throws GeneralSecurityException, IOException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, userEmail))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        // Create a folder for the user if it doesn't exist
//        String userFolderId = createUserFolderIfNotExists(userEmail, service);
//
//        // Set the parent folder for the file
//        File fileMetadata = new File();
//        fileMetadata.setName(fileName);
//        fileMetadata.setParents(Collections.singletonList(userFolderId));
//
//        // File's content.
//        ByteArrayContent mediaContent = ByteArrayContent.fromString("text/csv", csvData);
//
//        try {
//            File file = service.files().create(fileMetadata, mediaContent)
//                    .setFields("id")
//                    .execute();
//
//            System.out.println("CSV File ID: " + file.getId());
//        } catch (GoogleJsonResponseException e) {
//            System.err.println("Unable to upload CSV file: " + e.getDetails());
//            throw e;
//        }
//    }
    public void uploadData(String csvData, String fileName, String userEmail) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, userEmail))
                .setApplicationName(APPLICATION_NAME)
                .build();

        System.out.println("1");
        // Create a folder for the user if it doesn't exist
        String userFolderId = createUserFolderIfNotExists(userEmail, service);
        System.out.println("userfolderid" + userFolderId);

        String existingFileId = getFieldByName(fileName, userFolderId, service);

        if (existingFileId != null) {
            System.out.println("File with the same name exists. File ID" + existingFileId);

            // Update the existing file content
            service.files().delete(existingFileId).execute();
            System.out.println("Existing file updated successfully.");
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(userFolderId));

            // File's content.
            ByteArrayContent mediaContent = ByteArrayContent.fromString("text/csv", csvData);

            try {
                File file = service.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .execute();

                System.out.println("CSV File ID: " + file.getId());
                UserInfo userInfo = UserInfoService.getUserInfoByEmail(userEmail);
                userInfo.setFileId(file.getId());
                UserInfoService.saveUserInfo(userInfo);
            } catch (GoogleJsonResponseException e) {
                System.err.println("Unable to upload CSV file: " + e.getDetails());
                throw e;
            }
        } else {
            // Set the parent folder for the file
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(userFolderId));

            // File's content.
            ByteArrayContent mediaContent = ByteArrayContent.fromString("text/csv", csvData);

            try {
                File file = service.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .execute();

                System.out.println("CSV File ID: " + file.getId());
                UserInfo userInfo = UserInfoService.getUserInfoByEmail(userEmail);
                userInfo.setFileId(file.getId());
                UserInfoService.saveUserInfo(userInfo);
            } catch (GoogleJsonResponseException e) {
                System.err.println("Unable to upload CSV file: " + e.getDetails());
                throw e;
            }
        }
    }


    private String getFieldByName(String fileName, String folderId, Drive service) throws IOException {
        String query = "name='" + fileName + "' and '" + folderId + "' in parents";
        FileList result = service.files().list().setQ(query).execute();
        List<File> files = result.getFiles();

        System.out.println("Files matching the query: " + files);
        if (files != null && !files.isEmpty()) {
            return files.get(0).getId();
        }
        return null;
    }

    // Helper method to create a user-specific folder if not exists and return its ID
    private String createUserFolderIfNotExists(String userEmail, Drive service) throws IOException {
        String folderName = "UserFiles_" + userEmail;
        System.out.println("2");
        // Check if the folder exists
        String folderId = getFolderIdByName(folderName, service);
        System.out.println("3");
        System.out.println("folderid" + folderId);
         // If the folder doesn't exist, create it
        if (folderId == null) {
            File folderMetadata = new File();
            folderMetadata.setName(folderName);
            folderMetadata.setMimeType("application/vnd.google-apps.folder");
            System.out.println("7");
            File folder = service.files().create(folderMetadata)
                    .setFields("id")
                    .execute();

            folderId = folder.getId();
            System.out.println("fid  :" + folderId);
            UserInfo userInfo = UserInfoService.getUserInfoByEmail(userEmail);
            userInfo.setFolderId(folderId);
            UserInfoService.saveUserInfo(userInfo);
        }
        System.out.println("4");
        return folderId;
    }

    // Helper method to get the folder ID by name
    private String getFolderIdByName(String folderName, Drive service) {
        try {
            String query = "mimeType='application/vnd.google-apps.folder' and name='" + folderName + "'";
            System.out.println("5");
            FileList result = service.files().list().setQ(query).execute();
            System.out.println("6");
            System.out.println(result);
            List<File> folders = result.getFiles();

            if (folders != null && !folders.isEmpty()) {
                return folders.get(0).getId();
            }
        } catch (IOException e) {
            System.err.println("Error while getting folder ID by name: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



    public void downloadData(String email, String destinationPath) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, email))
                .setApplicationName(APPLICATION_NAME)
                .build();

        UserInfo userInfo = UserInfoService.getUserInfoByEmail(email);
        String folderId = userInfo.getFolderId();
        String fileId = userInfo.getFileId();

        System.out.println("Folder ID: " + folderId);
        System.out.println("File ID: " + fileId);

        // Use Drive.Files.List to search for the file in the specified folder
        FileList result = service.files().list()
                .setQ("'" + folderId + "' in parents and trashed=false")
                .execute();

        System.out.println("result== " + result);
        List<File> files = result.getFiles();

        if (files != null && !files.isEmpty()) {
            // Assuming there is only one file in the folder
            String fileToDownloadId = files.get(0).getId();

            System.out.println("File found. File ID: " + fileToDownloadId);

            InputStream contentStream = service.files().get(fileToDownloadId).executeMediaAsInputStream();

            // Save the content to a local file
            try (FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = contentStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("CSV file downloaded successfully to: " + destinationPath);
        } else {
            System.out.println("File not found in the specified folder.");
        }
    }

}
//    public void downloadData(String fileId, String folderId, String email, String destinationPath) throws GeneralSecurityException, IOException {
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, email))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        // Set the parent folder for the file
//        File fileMetadata = new File();
//        fileMetadata.setParents(Collections.singletonList(folderId));
//
//        InputStream contentStream = service.files().get(fileId).set("supportsAllDrives", true).set("includeItemsFromAllDrives", true).executeMediaAsInputStream();
//
//        // Save the content to a local file
//        try (FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = contentStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//            }
//        }
//
//        System.out.println("File downloaded successfully to: " + destinationPath);
//    }
//}

//    public static void main(String... args) throws IOException, GeneralSecurityException {
//

//        FileList result = service.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        List<File> files = result.getFiles();
//        if (files == null || files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            System.out.println("Files:");
//            for (File file : files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//            }
//        }
        // Upload file photo.jpg on drive.



