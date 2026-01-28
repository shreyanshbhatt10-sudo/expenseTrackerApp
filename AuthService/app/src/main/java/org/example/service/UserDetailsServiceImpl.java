package org.example.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomerUserDetails(user);
    }

    public UserInfo checkIfUserAlreadExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public String signupUser(UserInfoDto userInfoDto){
        //Define a function to check if userEmail, password is correct
        //ValidationUtil.validateUser() //to do
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        ValidationUtil.validateEmail(userInfoDto.getEmail());
        ValidationUtil.validatePassword(userInfoDto.getPassword());

        if(Objects.nonNull(checkIfUserAlreadExists(userInfoDto))){
            return null;
        }

        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(
                userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());

        userRepository.save(userInfo);
        //push Event to Queue
        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto, userId));
        return userId;
    }

    public String getUserByUsername(String userName){
        return Optional.of(userRepository.findByUsername(userName)).map(UserInfo::getUserId).orElse(null);
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }
}
