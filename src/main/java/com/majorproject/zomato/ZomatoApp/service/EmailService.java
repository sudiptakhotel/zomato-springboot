package com.majorproject.zomato.ZomatoApp.service;

import jakarta.mail.MessagingException;

import java.util.Map;
import java.util.Objects;

public interface EmailService {

     void sendOrderDetailsToPartner(String[] toEmail , String subject , String templateName ,
                                          Map<String , Object> templateModel) throws MessagingException;

     void sendOrderDetailsToPartner(String toEmail , String subject , String templateName ,
                                    Map<String , Object> templateModel) throws MessagingException;


}
