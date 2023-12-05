package com.contactmanager.springboot.services.imageUpload;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryImageUploadService {
    public Map upload(MultipartFile file, Integer userid);
}
