package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

     UserDetails loadUserByUsername(String username);

     boolean saveUser(User user);

     boolean saveUser(User user, List<String> rolesView);

     boolean saveUser(User user, Set<Role> roles);

     List<User> getListAllUsers();

     User findUserById(int id);

     void updateUser(User user, List<String> rolesView);

     void deleteUser(int id);
}
