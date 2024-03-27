package com.springsecurity.apirest.security;

import com.springsecurity.apirest.models.RoleModel;
import com.springsecurity.apirest.models.UserModel;
import com.springsecurity.apirest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    public Collection<GrantedAuthority> mapToAuthorities(List<RoleModel> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel users = userRepository.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new User(users.getUserName(), users.getPassword(), mapToAuthorities(users.getRoles()));
    }

}
