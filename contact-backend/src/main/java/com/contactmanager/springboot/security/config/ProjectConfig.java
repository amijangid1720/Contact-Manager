package com.contactmanager.springboot.security.config;

import com.cloudinary.Cloudinary;
import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.DoubleToIntFunction;

@Configuration
public class ProjectConfig {

    private String CLOUD_NAME="dlg1sejok";

    private String API_KEY="224748824966667";

    private String API_SECRET="7YLA_4QxQ7pZYhN5IRAt6H3iL7Q";



    @Bean
    public Cloudinary getCloudinary()
    {
        Map map=new HashMap();
        map.put("cloud_name",CLOUD_NAME);
        map.put("api_key",API_KEY);
        map.put("api_secret",API_SECRET);
        map.put("secure",true);
        return new Cloudinary(map);

    }
}
