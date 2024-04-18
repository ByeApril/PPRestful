package ru.kata.spring.boot_security.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Test {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Test(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        // создаю роли
        Role roleUser = new Role(1, "ROLE_USER");
        Role roleAdmin = new Role(2, "ROLE_ADMIN");
        roleService.addRole(roleUser);
        roleService.addRole(roleAdmin);

        // для входа
        userService.saveUser(new User("admin", "admin", 20, "admin@admin.com"),
                new HashSet<>(Set.of(roleAdmin, roleUser)));
        userService.saveUser(new User("user", "user",18, "user@user.com"));

    }
}