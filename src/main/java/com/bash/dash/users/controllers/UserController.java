package com.bash.dash.users.controllers;

import com.bash.dash.users.dtos.UpdateDto;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update")
    public ResponseEntity<MessageResponse> update(Long id){
        return ResponseEntity.ok(userService.delete(id));
    }

    @PostMapping("/delete")
    public ResponseEntity<MessageResponse> delete(UpdateDto dto, Long id){
        return ResponseEntity.ok(userService.update(dto, id));
    }
}
