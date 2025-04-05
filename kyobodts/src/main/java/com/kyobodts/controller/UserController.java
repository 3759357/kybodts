package com.kyobodts.controller;


import com.kyobodts.dto.UserDto;
import com.kyobodts.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;



    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto requestDto) {
        return ResponseEntity.ok(userService.createUser(requestDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable(name = "id") Long userId) {
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") Long id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

}