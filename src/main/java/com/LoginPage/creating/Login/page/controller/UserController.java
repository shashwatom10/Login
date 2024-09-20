package com.LoginPage.creating.Login.page.controller;

import com.LoginPage.creating.Login.page.model.UserCredential;
import com.LoginPage.creating.Login.page.model.UserInfo;
import com.LoginPage.creating.Login.page.service.UserService;
import exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        UserCredential userCredential = userService.login(email, password);
        Map<String, Object> response = new HashMap<>();

        if (userCredential != null) {
            System.out.println(userCredential);
            UserInfo userInfo = userService.getCurrentUser(userCredential).orElse(null);

            response.put("message", "Successful Login");
            if(userInfo!=null) {
                response.put("userRole", userInfo.getUserType());
            }
            response.put("userInfo", userInfo);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "User does not exist - Please register");
            return ResponseEntity.status(404).body(response);
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> register(@RequestBody UserInfo userInfo, @RequestParam String password) {
//        Map<String, String> response = new HashMap<>();
//        UserInfo createdUser = userService.register(userInfo, password);
//        response.put("message", "Registration Successful");
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> register(@RequestBody UserInfo userInfo) {
//        Map<String, String> response = new HashMap<>();
//
//        // Extract password from userCredential
//        String password = userInfo.getUserCredential().getPassword();
//
//        // Pass userInfo and password to the service
//        UserInfo createdUser = userService.register(userInfo, password);
//        response.put("message", "Registration Successful");
//
//        return ResponseEntity.ok(response);
//    }
@PostMapping("/register")
public ResponseEntity<Map<String, String>> register(@RequestBody UserInfo userInfo) {
    Map<String, String> response = new HashMap<>();

    try {
        String password = userInfo.getUserCredential().getPassword();
        UserInfo createdUser = userService.register(userInfo, password);
        response.put("message", "Registration Successful");
        return ResponseEntity.ok(response);
    } catch (UserAlreadyExistsException e) {
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}

    @GetMapping("/users")
    public List<UserInfo> getAllUsers() {
        return userService.getAllUsers();
    }



}