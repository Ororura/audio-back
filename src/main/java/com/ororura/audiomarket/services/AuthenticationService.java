package com.ororura.audiomarket.services;

import com.ororura.audiomarket.dtos.JwtResponse;
import com.ororura.audiomarket.dtos.SignInDTO;
import com.ororura.audiomarket.dtos.SignUpDTO;
import com.ororura.audiomarket.entities.user.Role;
import com.ororura.audiomarket.entities.user.UserEntity;
import com.ororura.audiomarket.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponse signUp(SignUpDTO request) {

        UserDetails user = User.builder()
                .username(request.getUsername())
                .roles("user")
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        UserEntity newUser = new UserEntity();
        newUser.setRole(Role.ROLE_USER);
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setResponse(jwtUtils.generateTokenFromUsername(user));

        userService.createUser(newUser);

        return jwtResponse;
    }

    public JwtResponse signIn(SignInDTO signInDTO) {
        UserDetails user = userService.loadUserByUsername(signInDTO.getUsername());
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setResponse(jwtUtils.generateTokenFromUsername(user));
        return jwtResponse;
    }
}
