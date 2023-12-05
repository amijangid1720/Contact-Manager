package com.contactmanager.springboot.services.imageUpload;
import java.io.IOException;
import java.util.*;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageUploadService{
    @Autowired
    private Cloudinary cloudinary;
    public Map upload(MultipartFile file, Integer userid){
        try {
            Map data=this.cloudinary.uploader().upload(file.getBytes(),Map.of());
            data.put("userid",userid);
            return data;

        } catch (IOException e) {
            throw new RuntimeException("Image Uploading Failed");
        }
    }
}
