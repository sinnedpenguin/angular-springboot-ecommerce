package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.dtos.UserDto;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.User;
import com.angularspringbootecommerce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
    }

    public User getUserById(Long id, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
        if (!user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied.", HttpStatus.BAD_REQUEST);
        }
        return user;
    }

    public User updateUserById(Long id, UserDto userDto, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (!user.getEmail().equals(authentication.getName())) {
            throw new AppException("Access denied", HttpStatus.BAD_REQUEST);
        }
        String newEmail = userDto.getEmail();
        String newPassword = userDto.getPassword();

        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            String hashedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedNewPassword);
        }
        userRepository.save(user);
        return user;
    }
}
