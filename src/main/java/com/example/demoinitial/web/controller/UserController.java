package com.example.demoinitial.web.controller;


import com.example.demoinitial.domain.User;
import com.example.demoinitial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;


@Controller
@RequestMapping(path="/users")
public class UserController {

    private final UserRepository userRepository;


    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/list")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "add-user";
        }

        trimUserStrings(user);

        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "eMail already exists", "eMail already exists");
            return "add-user";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "User name already exists", "User name already exists");
            return "add-user";
        }

        user.setPassword(user.getPassword());
        userRepository.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);

        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User updatedUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            updatedUser.setId(id);
            return "update-user";
        }

        trimUserStrings(updatedUser);

        // Check if new username does not exist
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (!user.getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                result.rejectValue("username", "User name already exists", "User name already exists");
                return "update-user";
            } else {
                user.setUsername(updatedUser.getUsername());
            }
        }

        // Check if new email does not exist
        if (!user.getEmail().equalsIgnoreCase(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                result.rejectValue("email", "eMail already exists", "eMail already exists");
                return "update-user";
            } else {
                user.setEmail(updatedUser.getEmail());
            }
        }

        user.setPassword(updatedUser.getPassword());
        userRepository.save(user);

        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);

        return "redirect:/users/list";
    }

    private void trimUserStrings (User user) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());
    }
}
