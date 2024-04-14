package com.twilioexample.controller;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber; // Adjusted import statement
import com.twilioexample.payload.SMSRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SMSController {

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostMapping("/send")
    public ResponseEntity<String> sendSMS(@RequestBody SMSRequest smsRequest) {
        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);

            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getTo()), // Adjusted import statement
                    new PhoneNumber(twilioPhoneNumber),
                    smsRequest.getBody()
            ).create();

            System.out.println("SMS sent with SID: " + message.getSid());

            return new ResponseEntity<>("SMS sent successfully", HttpStatus.OK);
        } catch (TwilioException e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            return new ResponseEntity<>("Failed to send SMS", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
