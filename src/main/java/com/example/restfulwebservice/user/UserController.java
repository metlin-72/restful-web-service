package com.example.restfulwebservice.user;

import com.example.restfulwebservice.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    private UserDAOService service;

    public UserController(UserDAOService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    // HATEOAS
    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id){
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("id[%s] 사용자가 없습니다.", id));
        }

        // HATEOAS 적용
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User savedUser = service.save(user);

        URI location =ServletUriComponentsBuilder
                        .fromCurrentRequestUri()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/users_v1")
    public ResponseEntity<User> createUser_v1(@RequestBody @Valid User user){
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("삭제할 id[%s] 사용자가 없습니다.", id));
        }
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        User updatedUser = service.updateUser(user);

        if(updatedUser == null) {
            throw new UserNotFoundException(String.format("수정할 id[%s] 사용자가 없습니다.", user.getId()));
        }

        return updatedUser;

    }
}
