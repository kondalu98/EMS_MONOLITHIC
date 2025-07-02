package com.event.controller;


import com.event.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final JwtUtil jwtService;

    public AdminController(JwtUtil jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> adminLogin(@RequestParam String email, @RequestParam String password) {
        if (email.equals(adminEmail) && password.equals(adminPassword)) {
            String token = jwtService.generateToken(email);

            Map<String, String> response = Map.of("token", token, "email", email);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = Map.of("error", "Invalid admin credentials");
            return ResponseEntity.status(401).body(errorResponse);


        }
    }
}