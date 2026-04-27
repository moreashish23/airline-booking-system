package com.airline.service;

import com.airline.model.User;
import com.airline.payload.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserByEmail(String email) throws Exception;
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();

}
