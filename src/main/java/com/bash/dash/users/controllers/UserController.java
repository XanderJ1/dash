package com.bash.dash.users.controllers;

import com.bash.dash.users.domain.User;
import com.bash.dash.users.dtos.UpdateDto;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getProfile() {
        return new ResponseEntity<User>(userService.findMe(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> update(UpdateDto dto, Long id){
        return ResponseEntity.ok(userService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id){
        return ResponseEntity.ok(userService.delete(id));
    }


}
