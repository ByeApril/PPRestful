package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
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
    public String adminHome(Model model) {
        model.addAttribute("users", userService.getListAllUsers());
        return "all_users";
    }

    @GetMapping("/all_users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getListAllUsers());
        return "all_users";
    }

    @GetMapping("/add_user")
    public String showFormForAdd(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "add_user";
    }

    @PostMapping("/save_user")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "roles1", required = false) List<String> roleName, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "add_user";
        }
        boolean isUserSaved;
        if (roleName == null) {
            isUserSaved = userService.saveUser(user);
        } else {
            isUserSaved = userService.saveUser(user, roleName);
        }
        if (isUserSaved) {
            return "redirect:/admin/all_users";
        } else {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("errorMessage", "User not found");
            return "add_user";
        }
    }

    @GetMapping("/update")
    public String showFormForUpdate(@RequestParam("userId") int id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found");
            List<User> users = userService.getListAllUsers();
            model.addAttribute("users", users);
            return "all_users";
        }
        model.addAttribute("user", user);
        return "update_user";
    }

    @GetMapping("/{id}/edit")
    public String updateUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.findAll());
        return "update_user";
    }

    @PostMapping("/update_user")
    public String saveUpdateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "rolesView", required = false) List<String> rolesView, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "update_user";
        }
        userService.updateUser(user, rolesView);
        return "redirect:/admin/all_users";
    }

    @PostMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/all_users";
    }
}
