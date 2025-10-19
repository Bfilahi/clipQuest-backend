package com.filahi.springboot.clipquest.controller;


import com.filahi.springboot.clipquest.response.UserResponse;
import com.filahi.springboot.clipquest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User REST API Endpoints", description = "Operations related to the current user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "User information", description = "Get current user information")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info")
    public UserResponse getUserInfo() {
        return userService.getUserInfo();
    }

    @Operation(summary = "Delete user", description = "Delete current user from database")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public void delete(){
        userService.deleteUser();
    }

}
