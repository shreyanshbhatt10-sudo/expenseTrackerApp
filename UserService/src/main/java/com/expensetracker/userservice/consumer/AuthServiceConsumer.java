package com.expensetracker.userservice.consumer;

import com.expensetracker.userservice.entities.UserInfoDto;
import com.expensetracker.userservice.repository.UserRepository;
import com.expensetracker.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class AuthServiceConsumer {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    AuthServiceConsumer(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(UserInfoDto eventData){
        try{
            userService.createOrUpdateUser(eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
