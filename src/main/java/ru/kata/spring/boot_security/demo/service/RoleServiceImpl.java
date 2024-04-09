package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Transactional
    public void addRole(Role role) {
        roleRepository.save(role);
    }
    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    @Transactional(readOnly = true)
    @Override
    public Set<Role> findByRoleNameIn(List<String> roles) {
        return roleRepository.findByRoleNameIn(roles);
    }
}