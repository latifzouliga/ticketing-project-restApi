package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProjectService projectService, TaskService taskService, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String username) throws TicketingProjectException {
        User user = userRepository
//                .findByUserName(username).
//                orElseThrow(() -> new TicketingProjectException("User can not be found"));;
                .findAll().stream().filter(u -> u.getUserName().equals(username)).findFirst().get();
        return userMapper.convertToDTO(user);
    }

    @Override
    public void save(UserDTO userDTO) {

        userDTO.setEnabled(true);

        User obj = userMapper.convertToEntity(userDTO);

        userRepository.save(obj);
        keycloakService.userCreate(userDTO);

    }

    @Override
    public UserDTO update(UserDTO dto) throws TicketingProjectException {

       //Find current user
        User user = userRepository
                .findByUserName(dto.getUserName()).
                orElseThrow(() -> new TicketingProjectException("User can not be found"));;
        //Map updated user dto to entity object
        User convertedUser = userMapper.convertToEntity(dto);
        //set id to converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);

    }

    @Override
    public void delete(String username) throws TicketingProjectException {
        User user = userRepository
                .findByUserName(username).
                orElseThrow(() -> new TicketingProjectException("User can not be found"));;

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
            keycloakService.delete(username);
        }else {
            throw new TicketingProjectException("User can not deleted");
        }


    }

    private boolean checkIfUserCanBeDeleted(User user) {

        List<ProjectDTO> projectDTOList = projectService.readAllByAssignedManager(user);
        List<TaskDTO> taskDTOList = taskService.readAllByAssignedEmployee(user);

        switch (user.getRole().getDescription()) {
            case "Manager":
                return projectDTOList.isEmpty();
            case "Employee":
                return taskDTOList.isEmpty();
            default:
                return true;
        }

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);

        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }
}
