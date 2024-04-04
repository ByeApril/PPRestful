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
    private final RoleService roleSerivce;

    @Autowired
    public Test(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleSerivce = roleService;
    }

    @PostConstruct
    public void init() {
        Role roleUser = new Role(1, "ROLE_USER");
        Role roleAdmin = new Role(2, "ROLE_ADMIN");
        roleSerivce.addRole(roleUser);
        roleSerivce.addRole(roleAdmin);

        userService.saveUser(new User("admin", "admin", 20, "admin@admin.com"),
                new HashSet<Role>(Set.of(roleAdmin, roleUser)));
        userService.saveUser(new User("user", "user", 18, "user@user.com"));
    }
}
