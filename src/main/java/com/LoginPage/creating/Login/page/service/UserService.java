package com.LoginPage.creating.Login.page.service;

import com.LoginPage.creating.Login.page.model.UserCredential;
import com.LoginPage.creating.Login.page.model.UserInfo;
import com.LoginPage.creating.Login.page.repositiory.UserCredentialRepository;
import com.LoginPage.creating.Login.page.repositiory.UserInfoRepository;
import exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserCredential login(String email, String password) {
        Optional<UserCredential> user = userCredentialRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null; // Handle login failure in the controller
    }

//    public UserInfo register(UserInfo userInfo, String password) {
//        // Hash password
//        String hashedPassword = passwordEncoder.encode(password);
//
//        // Create UserCredential and UserInfo objects
//        UserCredential userCredential = new UserCredential();
//        userCredential.setEmail(userInfo.getUserCredential().getEmail());
//        userCredential.setPassword(hashedPassword);
//
//        // Save UserCredential
//        userCredential = userCredentialRepository.save(userCredential);
//
//        // Set the userCredential in UserInfo and save
//        userInfo.setUserCredential(userCredential);
//        return userInfoRepository.save(userInfo);
//    }

    public UserInfo register(UserInfo userInfo, String password) {
        // Check if the user already exists
        Optional<UserCredential> existingUser = userCredentialRepository.findByEmail(userInfo.getUserCredential().getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(password);

        // Create UserCredential and UserInfo objects
        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(userInfo.getUserCredential().getEmail());
        userCredential.setPassword(hashedPassword);

        // Save UserCredential
        userCredential = userCredentialRepository.save(userCredential);

        // Set the userCredential in UserInfo and save
        userInfo.setUserCredential(userCredential);
        return userInfoRepository.save(userInfo);
    }

    //public UserInfo register(UserInfo userInfo, String password) {
//    // Check if the email already exists
//    Optional<UserCredential> existingUser = userCredentialRepository.findByEmail(userInfo.getUserCredential().getEmail());
//
//    if (existingUser.isPresent()) {
//        throw new UserAlreadyExistsException("User already exists");
//    }
//
//    // Proceed with registration
//    UserCredential newCredential = new UserCredential();
//    newCredential.setEmail(userInfo.getUserCredential().getEmail());
//    newCredential.setPassword(password); // Hash the password before saving in production
//
//    userCredentialRepository.save(newCredential);
//
//    userInfo.setEmail(userInfo.getUserCredential().getEmail());
//    userInfoRepository.save(userInfo);
//
//    return userInfo;
//}
    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    public Optional<UserInfo> getCurrentUser(UserCredential userCredential) {
        // Check if the userCredential is valid and exists
        Optional<UserCredential> existingCredential = userCredentialRepository.findByEmail(userCredential.getEmail());
        System.out.println(userCredential.getEmail());
        if (existingCredential.isPresent()) {
            // Fetch the UserInfo associated with the found UserCredential
            return userInfoRepository.findById(userCredential.getId());
//            return userInfoRepository.findById(existingCredential.get().getId());
            // Return null if no matching UserInfo is found
        }

        return null; // Return null if the userCredential does not exist
    }




}
