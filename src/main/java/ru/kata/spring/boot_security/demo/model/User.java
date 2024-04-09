package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //    @NotEmpty(message = "Имя не может быть пустым")
//    @Pattern(regexp = "^[A-Za-zА-Яа-я]+$", message = "Имя должно содержать только буквы")
//    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 25 букв")
    @Column(name = "name") //
    private String name; //
    //    @NotNull(message = "Возраст не может быть пустым")
//    @Min(value = 1, message = "Возраст должен быть больше 0")
//    @Max(value = 95, message = "Возраст должен быть меньше 95")
    @Column(name = "age")
    private int age;

    //    @NotEmpty(message = "Email не может быть пустым")
//    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{1,3}$", message = "Неверный формат email")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
//    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String name, String password, int age, String email) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.email = email;
    }

    public User(String name, String password, int age, String email, Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}