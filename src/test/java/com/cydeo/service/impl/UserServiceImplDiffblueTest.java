package com.cydeo.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.enums.Gender;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplDiffblueTest {
    @MockBean
    private KeycloakService keycloakService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#deleteByUserName(String)}
     */
    @Test
    void testDeleteByUserName() {
        // Arrange
        doNothing().when(userRepository).deleteByUserName(Mockito.<String>any());

        // Act
        userServiceImpl.deleteByUserName("janedoe");

        // Assert that nothing has changed
        verify(userRepository).deleteByUserName(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#delete(String)}
     */
    @Test
    void testDelete() throws TicketingProjectException {
        // Arrange
        Role role = new Role();
        role.setDescription("The characteristics of someone or something");
        role.setId(1L);
        role.setInsertDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        role.setInsertUserId(1L);
        role.setIsDeleted(true);
        role.setLastUpdateDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        role.setLastUpdateUserId(1L);

        User user = new User();
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setGender(Gender.MALE);
        user.setId(1L);
        user.setInsertDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setInsertUserId(1L);
        user.setIsDeleted(true);
        user.setLastName("Doe");
        user.setLastUpdateDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setLastUpdateUserId(1L);
        user.setPassWord("Pass Word");
        user.setPhone("6625550144");
        user.setRole(role);
        user.setUserName("janedoe");
        Optional<User> ofResult = Optional.of(user);

        Role role2 = new Role();
        role2.setDescription("The characteristics of someone or something");
        role2.setId(1L);
        role2.setInsertDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        role2.setInsertUserId(1L);
        role2.setIsDeleted(true);
        role2.setLastUpdateDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        role2.setLastUpdateUserId(1L);

        User user2 = new User();
        user2.setEnabled(true);
        user2.setFirstName("Jane");
        user2.setGender(Gender.MALE);
        user2.setId(1L);
        user2.setInsertDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setInsertUserId(1L);
        user2.setIsDeleted(true);
        user2.setLastName("Doe");
        user2.setLastUpdateDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setLastUpdateUserId(1L);
        user2.setPassWord("Pass Word");
        user2.setPhone("6625550144");
        user2.setRole(role2);
        user2.setUserName("janedoe");

        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByUserName(Mockito.<String>any())).thenReturn(ofResult);
        when(projectService.readAllByAssignedManager(Mockito.<User>any())).thenReturn(new ArrayList<>());
        when(taskService.readAllByAssignedEmployee(Mockito.<User>any())).thenReturn(new ArrayList<>());
        doNothing().when(keycloakService).delete(Mockito.<String>any());

        // Act
        userServiceImpl.delete("janedoe");

        // Assert
        verify(userRepository).findByUserName(Mockito.<String>any());
        verify(keycloakService).delete(Mockito.<String>any());
        verify(projectService).readAllByAssignedManager(Mockito.<User>any());
        verify(taskService).readAllByAssignedEmployee(Mockito.<User>any());
        verify(userRepository).save(Mockito.<User>any());
    }
}
