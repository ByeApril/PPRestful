package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {


    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model, @AuthenticationPrincipal User user) {
        List<User> users = userService.getListAllUsers();
        if (!users.isEmpty()) {
            model.addAttribute("user", users.get(0));
        }
        model.addAttribute("users", users);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("userEmpty", new User());
        model.addAttribute("thisUser", user);
        return "all_users";
    }
    `
    @PostMapping("/save_user")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "rolesController", required = false) List<String> rolesView) {
        userService.saveUser(user, rolesView);
        return "redirect:/admin";
    }


    @PatchMapping("/update/{id}")
    public String saveUpdateUser(@PathVariable("id") int id, @ModelAttribute("user") User user, @RequestParam(value = "rolesController", required = false) List<String> rolesView) {
        User existingUser = userService.findUserById(id);

        if (existingUser == null) {
            return "redirect:/admin";
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getAge() != 0) {
            existingUser.setAge(user.getAge());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (rolesView != null) {
            existingUser.setRoles(roleService.findByRoleNameIn(rolesView));
        }

        userService.saveUser(existingUser);

        return "redirect:/admin";
    }





    @PostMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id, @ModelAttribute("user") User user) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
