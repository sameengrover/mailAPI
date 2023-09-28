package com.email.service;

import com.email.config.FileUploadProperties;
import lombok.Value;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public boolean sendEmail(String subject, String message, String to){
        boolean flag = false;
        String from ="sameengrover12@gmail.com";
//        Variable for the gmail
        String host = "smtp.gmail.com";
//        get the system properties
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES: "+properties);
//        Setting important information to properties object
//        host Set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

//        Step1: To get the session object

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sameengrover12@gmail.com", "fbiz vuyp wrty ykbl");
            }
        });

        session.setDebug(true);

//        Step2: Compose the message(text, multimedia)
        MimeMessage m = new MimeMessage(session);
        try{
//            from Email
            m.setFrom(from);
//            adding recepient to message
            m.addRecipient(Message.RecipientType.TO , new InternetAddress(to));
//            adding subject to message
            m.setSubject(subject);
//            adding text to message
            m.setText(subject);
//            Step3: send the message using transport class
            Transport.send(m);
            System.out.println("Sent success.............");
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

//    --------- To upload a file in our local directory----------
    @Autowired
    public FileUploadProperties fileUploadProperties;
    public boolean uploadFile(MultipartFile file) {
        try {
            String uploadDir = fileUploadProperties.getDir();
            // Check if the upload directory exists, create it if not
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Save the uploaded file to the specified directory
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    ----------- Sending Email with Attachment ---------------
    public void sendEmailWithAttachment(String to, String subject, String message, MultipartFile attachment)
            throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);

        // Attach the file
        helper.addAttachment(attachment.getName(), attachment);

        javaMailSender.send(mimeMessage);
    }
}


