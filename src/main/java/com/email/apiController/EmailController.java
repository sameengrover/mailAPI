package com.email.apiController;

import com.email.config.FileUploadProperties;
import com.email.model.EmailRequest;
import com.email.service.EmailService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

@RestController
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @RequestMapping("/welcome")
    public String Welcome(){
        return "Hello, this is my email API";
    }
//    ----------- For sending the normal Email-----------
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest){
        log.info("==> INSIDE: Email Controller--> sendEmail() <==");
        System.out.println(emailRequest);
       boolean flag = this.emailService.sendEmail(emailRequest.getSubject(), emailRequest.getMessage(),emailRequest.getTo());
        if(flag) return ResponseEntity.ok("Email has been sent.......");
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email not sent.......");

    }

//    -------- To save the file in directory-----------
    @PostMapping("/saveFileIntoDirectory")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        boolean uploadSuccessful = emailService.uploadFile(file);

        if (uploadSuccessful) {
            return ResponseEntity.ok("File uploaded successfully");
        } else {
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

//    ------ To send the Email With Attachment using form-data in postman-----------
    @PostMapping("/sendEmailWithAttachment")
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam("attachment") MultipartFile attachment) {
        try {
            emailService.sendEmailWithAttachment(to, subject, message, attachment);
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            log.error("Email sending failed.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email sending failed.");
        }
    }
}