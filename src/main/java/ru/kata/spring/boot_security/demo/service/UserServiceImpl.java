package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        User user = userOptional.get();
        user.getRoles().size();
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public User findUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    @Transactional
    public List<User> getListAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean saveUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }


    @Transactional
    public boolean saveUser(User user, Set<Role> roles) {
        if (userRepository.findUserByName(user.getEmail()).isPresent()) {
            return false;
        }
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean saveUser(User user, List<String> rolesView) {
        if (userRepository.findUserByName(user.getEmail()).isPresent()) {
            return false;
        }

        Set<Role> roles;
        if (rolesView == null || rolesView.isEmpty()) {
            roles = Collections.singleton(new Role(1, "ROLE_USER"));
        } else {
            roles = roleService.findByRoleNameIn(rolesView);
        }

        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void updateUser(User user, List<String> rolesView) {

        if (user.getPassword() != null ) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (rolesView == null) {
            user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        } else {
            user.setRoles(roleService.findByRoleNameIn(rolesView));
        }

        userRepository.save(user);
    }





    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);

    }
}


