package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = service.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new UserDetailsImpl(user.get());
    }

}
