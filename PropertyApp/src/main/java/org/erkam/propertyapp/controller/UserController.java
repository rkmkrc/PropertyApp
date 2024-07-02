package org.erkam.propertyapp.controller;

import lombok.RequiredArgsConstructor;
import org.erkam.propertyapp.dto.request.UserSaveRequest;
import org.erkam.propertyapp.dto.response.GenericResponse;
import org.erkam.propertyapp.dto.response.UserSaveResponse;
import org.erkam.propertyapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<GenericResponse<UserSaveResponse>> addUser(@RequestBody UserSaveRequest request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.OK);
    }
}
