package com.email.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmailRequest {
    String filePath;
    String to;
    String subject;
    String message;

//    public EmailRequest(String to, String subject, String message) {
//        this.to = to;
//        this.subject = subject;
//        this.message = message;
//    }

//    public emailRequest(String path, String to,String subject, String message ){
//        this.to = to;
//        this.path = path;
//
//    }
    public EmailRequest() {

    }

//    public EmailRequest(String filePath, String to, String subject, String message){
//        this.filePath = filePath;
//        this.to = to;
//        this.message = message;
//        this.subject = subject;
//    }
}
