package com.safezones.safezones.users;

import com.safezones.safezones.UserRepository;
import com.safezones.safezones.PointRepository;
import com.safezones.safezones.points.Point;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam(name="name") String name,
                                            @RequestParam(name="email") String email,
                                            @RequestParam(name="id") String id) {

        User user = new User();
        user.setId(id);
        user.setUsername(name);
        user.setEmail(email);
        user.setLevel(1);
        user.setRewardPoints(0);
        user.setRegisterDate(new Date());
        //System.out.println(name);
        //System.out.println(email);
        //System.out.println(id);
        userRepository.save(user);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get().getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path="/level/{id}")
    public ResponseEntity<?> getUserLevel(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get().getLevel());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path="/points/{id}")
    public ResponseEntity<?> getUserPoints(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get().getRewardPoints());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }




    @PutMapping(path="/incrementPoints/{id}")
    @Transactional
    public ResponseEntity<String> incrementRewardPoints(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRewardPoints(user.getRewardPoints() + 1);
            switch (user.getLevel()){
                case 1: {
                    if (user.getRewardPoints() == 10){
                        user.setRewardPoints(0);
                        user.setLevel(2);
                    }
                }
                case 2: {
                    if (user.getRewardPoints() == 20){
                        user.setRewardPoints(0);
                        user.setLevel(3);
                    }
                }
                case 3: {
                    if (user.getRewardPoints() == 30){
                        user.setRewardPoints(0);
                        user.setLevel(4);
                    }
                }
                case 4: {
                    if (user.getRewardPoints() == 40){
                        user.setRewardPoints(0);
                        user.setLevel(5);
                    }
                }
            }
            userRepository.save(user);
            return ResponseEntity.ok("Points incremented by 1");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}