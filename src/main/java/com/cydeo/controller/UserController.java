package com.cydeo.controller;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/user")
@Tag(name = "User",description = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //@ExecutionTime
    @GetMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Get Users")
    public ResponseEntity<ResponseWrapper> getUsers() {

        List<UserDTO> userDTOList = userService.listAllUsers();
        String message = userDTOList.isEmpty() ? "There are no users to show" : "Users retrieved Successfully";

        return ResponseEntity.ok(
                new ResponseWrapper(message, userDTOList, HttpStatus.OK)
        );
    }

    @ExecutionTime
    @GetMapping("/{userName}")
    @RolesAllowed("Admin")
    @Operation(summary = "Get User By Username")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("userName") String userName) throws TicketingProjectException {
        UserDTO user = userService.findByUserName(userName);
        return ResponseEntity.ok(
                new ResponseWrapper("User is successfully retrieved",user,HttpStatus.OK)
        );
    }


    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ResponseWrapper("A new Spartan is created", userDTO, HttpStatus.CREATED)
                );
    }


    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update User")
    public ResponseEntity<ResponseWrapper> updateUser( @Valid @RequestBody UserDTO user) throws TicketingProjectException {
        userService.update(user);
        return ResponseEntity.ok(
                new ResponseWrapper("User is successfully updated",user,HttpStatus.OK)
        );
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete user")
    @DefaultExceptionMessage(defaultMessage = "Failed to delete user bro")
    private ResponseEntity<ResponseWrapper> deleteUser(@PathVariable String username) throws TicketingProjectException {
        userService.delete(username);
        return ResponseEntity.ok(
                new ResponseWrapper("User deleted Successfully", HttpStatus.NO_CONTENT)
        );
        // return ResponseEntity.status(HttpStatus.NO_CONTENT)
        //       .body(new ResponseWrapper("User deleted Successfully", HttpStatus.NO_CONTENT));
    }


}
