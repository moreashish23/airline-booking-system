package com.airline.service.impl;

import com.airline.mapper.UserMapper;
import com.airline.model.User;
import com.airline.payload.dto.UserDTO;
import com.airline.repository.UserRepository;
import com.airline.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new Exception("User not found with email: " + email);
        }
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id: " + id)
        );

        return  UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> user = userRepository.findAll();
        return UserMapper.toDTOList(user);
    }
}
