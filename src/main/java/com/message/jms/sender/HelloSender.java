package com.message.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.message.jms.config.JmsConfig;
import com.message.jms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {
    
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    
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
    
    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage () throws JMSException {
        System.out.println ("Sending first message..");
        
        HelloWorldMessage message = HelloWorldMessage
                .builder ()
                .id (UUID.randomUUID ())
                .message ("Hello")
                .build ();
        
        Message receive = jmsTemplate.sendAndReceive (JmsConfig.MY_SEND_RCV_QUEUE, new MessageCreator () {
            @Override
            public Message createMessage (Session session) throws JMSException {
                Message helloMessage = null;
                try {
                    helloMessage = session.createTextMessage (objectMapper.writeValueAsString (message));
                    helloMessage.setStringProperty ("_type", "com.message.jms.model.HelloWorldMessage");
                    
                    System.out.println ("Sending and Receive!");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException (e.getMessage ());
                }
                
                
            }
        });
        System.out.println (receive.getBody (String.class));
    }
}
