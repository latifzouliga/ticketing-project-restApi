package com.cydeo.service;


import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;

import java.util.List;

public interface UserService{

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username) throws TicketingProjectException;
    void save(UserDTO dto);
    UserDTO update(UserDTO dto) throws TicketingProjectException;
    void deleteByUserName(String username);
    void delete(String username) throws TicketingProjectException;
    List<UserDTO> listAllByRole(String role);





}
