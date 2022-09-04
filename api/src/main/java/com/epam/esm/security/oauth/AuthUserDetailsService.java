package com.epam.esm.security.oauth;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.RequestParamName.EMAIL;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type JwtUserDetailsService.
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {
    public static final String ROLE_PREFIX = "ROLE_";
    private final UserService userService;

    /**
     * Instantiates a new JwtUserDetailsService.
     *
     * @param userService the user service
     */
    @Autowired
    public AuthUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String email) {
        List<User> users = userService.findUsers(Map.of(EMAIL, email)).getContent();
        if (users.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        User user = users.get(0);
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getUserRole());
    }
}