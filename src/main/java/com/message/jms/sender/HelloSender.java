package com.message.jms.sender;

import com.message.jms.config.JmsConfig;
import com.message.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {
    
    private final JmsTemplate jmsTemplate;
    
    @Scheduled(fixedRate = 2000)
    public void sendMessage () {
        System.out.println ("Sending first message..");
        
        HelloWorldMessage message = HelloWorldMessage
                .builder ()
                .id (UUID.randomUUID ())
                .message ("Hellow World")
                .build ();
        
        jmsTemplate.convertAndSend (JmsConfig.MY_QUEUE, message);
        System.out.println ("Message Sent");
    }
}
