package com.contactmanager.springboot.security.services;


import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String subject, String message, String to){


        Boolean f = false;

        String from="mkumari@argusoft.com";
        //variable for mail
        String host="smtp.gmail.com";


        //get the system properties
        Properties properties = System.getProperties();
        System.out.println("Properties" + properties);

        //setting important information to properties object

        //host set
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        //to get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("mkumari@argusoft.com","");
            }
        });
        session.setDebug(true);

        //to compose the message
        MimeMessage m = new MimeMessage(session);

        try {
            //from email
            m.setFrom(from);


        //adding receipents to message
            m.setRecipient(Message.RecipientType.TO, new InternetAddress(to.replace("\"", "")));



            //adding suject to message

       m.setSubject(subject);
            //adding texts to message
            m.setText(message);

            //send

        //step 3:  send the message using transport class
        Transport.send(m);
        System.out.println("sent succecss...........");
        f=true;
        }
        catch (Exception e){
             e.printStackTrace();

        }
           return  f;

    }
}
