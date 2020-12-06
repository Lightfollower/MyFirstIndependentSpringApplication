package com.test.task.services;

import com.test.task.entities.Role;
import com.test.task.entities.dtos.SystemUser;
import com.test.task.entities.User;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.repositories.UserRepository;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public Optional<User> findByName(String name) {
        return userRepository.findOneByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByName(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public User saveOrUpdate(User user) {
        User temp = userRepository.findById(user.getId()).get();
        user.setName(temp.getName());
        user.setPassword(temp.getPassword());
        return userRepository.save(user);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById((long) id);
    }

    @Transactional
    public void save(SystemUser systemUser) {
        User user = new User();
        if (existByName(systemUser.getName()))
            throw new MalformedEntityException(String.format(Constants.NAME_IS_BUSY, systemUser.getName()));
        user.setName(systemUser.getName());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setRoles(Arrays.asList(roleService.findByName("admin")));
    }

    private boolean existByName(String name) {
        return userRepository.existsByName(name);
    }
}