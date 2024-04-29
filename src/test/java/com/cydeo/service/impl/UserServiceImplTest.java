package com.cydeo.service.impl;


import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KeycloakServiceImpl keycloakService;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private ProjectServiceImpl projectService;
    @Mock
    private TaskServiceImpl taskService;


    @Spy
    private UserDTO userDTO = new UserDTO();
    @Spy
    private User user = new User();


    @Test
    void listAllUsers_test() {

        // given

        List<User> userList = List.of(new User());


        when(userRepository.findAll(Sort.by("firstName"))).thenReturn(userList);
        when(userMapper.convertToDTO(new User())).thenReturn(new UserDTO());

        // when
        List<UserDTO> returedUserDTOList = userService.listAllUsers();

        // then
        verify(userRepository).findAll(Sort.by("firstName"));
        verify(userMapper).convertToDTO(new User());

        assertNotNull(returedUserDTOList);
    }

    @ParameterizedTest
    @ValueSource(strings = {"mike", "james", "jamal"})
    void findUserByUserName_test(String username) throws TicketingProjectException {

        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(userMapper.convertToDTO(user)).thenReturn(userDTO);

        UserDTO returedUserDto = userService.findByUserName(username);

        verify(userRepository).findByUserName(username);
        verify(userMapper).convertToDTO(user);

        assertNotNull(returedUserDto);

    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     dddd d"})
    void findUserByUserNameThrowsException_test(String username) throws TicketingProjectException {

        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(TicketingProjectException.class, () -> userService.findByUserName(username));

        verify(userRepository).findByUserName(username);

        assertEquals("User can not be found", exception.getMessage());

    }

    @Test
    void createUser_test() {

//        UserDTO userDTO = new UserDTO();
//        User user = new User();
        userDTO.setPassWord("plainPassword");


        when(userMapper.convertToEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userDTO);

        verify(userDTO).setEnabled(true);
        verify(passwordEncoder).encode("plainPassword");
        verify(user).setPassWord("encodedPassword");
        verify(userRepository).save(user);
        verify(keycloakService).userCreate(userDTO);
    }

    @Test
    void update() throws TicketingProjectException {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("username");

        User exestingUser = new User();
        exestingUser.setId(1L);

        User convertedUser = new User();
        convertedUser.setId(1L);

        when(userRepository.findByUserName("username")).thenReturn(Optional.of(exestingUser));
        when(userMapper.convertToEntity(userDTO)).thenReturn(convertedUser);
        when(userRepository.save(convertedUser)).thenReturn(convertedUser);

        userService.update(userDTO);

        assertEquals(convertedUser, exestingUser);
        verify(userRepository, times(2)).findByUserName("username");
        verify(userRepository).save(convertedUser);
        verify(userMapper).convertToEntity(userDTO);


    }


    @Test
    void deleteByUserName() {

        doNothing().when(userRepository).deleteByUserName("username");

        userService.deleteByUserName("username");

        verify(userRepository).deleteByUserName("username");

    }



    @Test
    void delete() throws TicketingProjectException {

        Role role = new Role();
        role.setDescription("Employee");
        String username = "testUser";

        User user = new User();
        user.setId(1L);
        user.setUserName(username);
        user.setRole(role);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(keycloakService).delete(username);
        when(projectService.readAllByAssignedManager(user)).thenReturn(Collections.emptyList());
        when(taskService.readAllByAssignedEmployee(user)).thenReturn(Collections.emptyList());

        // Act
        userService.delete(username);

        // Assert
        assertTrue(user.getIsDeleted());
        verify(userRepository).save(user);
        verify(keycloakService).delete(username);
    }

    @Test
    void listAllByRole() {
    }
}


