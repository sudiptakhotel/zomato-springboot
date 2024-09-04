package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SmsServiceImpl implements SmsService {


    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;

    // Constructor injection for all required properties
    public SmsServiceImpl(@Value("${twilio.accountSid}") String accountSid,
                          @Value("${twilio.authToken}") String authToken,
                          @Value("${twilio.phoneNumber}") String fromPhoneNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhoneNumber = fromPhoneNumber;
        Twilio.init(accountSid, authToken); // Initialize Twilio with account SID and auth token
    }

    @Override
    public void sendSms(String to, String message) {
        Message.creator(
                        new PhoneNumber(to), // Recipient phone number
                        new PhoneNumber(fromPhoneNumber), // Sender phone number
                        message) // SMS message content
                .create(); // Send SMS
    }
}
