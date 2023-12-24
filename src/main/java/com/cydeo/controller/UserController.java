package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User",description = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Get User by username")
    private ResponseEntity<ResponseWrapper> getUser(@PathVariable String username) {
        return ResponseEntity.ok(
                new ResponseWrapper("User retrieved Successfully", userService.findByUserName(username), HttpStatus.OK)
        );
    }


    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ResponseWrapper("A new Spartan is created", userDTO, HttpStatus.CREATED)
                );
    }


    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update user")
    private ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseEntity.ok(
                new ResponseWrapper("User updated Successfully", userDTO, HttpStatus.OK)
        );
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete user")
    private ResponseEntity<ResponseWrapper> deleteUser(@PathVariable String username) {
        userService.deleteByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("User deleted Successfully", HttpStatus.NO_CONTENT));
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .body(new ResponseWrapper("User deleted Successfully", HttpStatus.NO_CONTENT));
    }

}
