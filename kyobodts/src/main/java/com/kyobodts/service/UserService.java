package com.kyobodts.service;

import com.kyobodts.dto.UserDto;
import com.kyobodts.entity.User;
import com.kyobodts.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
    UserRepository userRepository;



    public UserDto createUser(UserDto requestDto) {
        User user = User.builder()
                .password(requestDto.getPassword())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .build();
        User registerUser = userRepository.save(user);
        return UserDto.builder()
                .id(registerUser.getId())
                .password(registerUser.getPassword())
                .name(registerUser.getName())
                .email(registerUser.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 유저가 존재하지 않습니다: " + id));

        return UserDto.builder()
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }
}
