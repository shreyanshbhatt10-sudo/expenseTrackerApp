package org.example.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.eventProducer.UserInfoEvent;
import org.example.eventProducer.UserInfoProducer;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

//@Component
//@AllArgsConstructor
//@Data
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final UserInfoProducer userInfoProducer;

    // Standard Constructor for your @Bean method to use
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserInfoProducer userInfoProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoProducer = userInfoProducer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("could not found user..!!");
        }
        return new CustomerUserDetails(user);
    }

    public UserInfo checkIfUserAlreadExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        //Define a function to check if userEmail, password is correct
        //ValidationUtil.validateUser() //to do
        ValidationUtil.validateEmail(userInfoDto.getEmail());
        ValidationUtil.validatePassword(userInfoDto.getPassword());

        if(Objects.nonNull(checkIfUserAlreadExists(userInfoDto))){
            return false;
        }

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(
                userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());

        userRepository.save(userInfo);
        //push Event to Queue
        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto, userId));
        return true;
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }
}
