package com.epam.esm.security.oauth;

import com.epam.esm.entity.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type CustomUserDetails.
 */
public class CustomUserDetails implements UserDetails {
    /**
     * The constant ROLE_PREFIX.
     */
    public static final String ROLE_PREFIX = "ROLE_";
    private long userId;
    private String username;
    private String password;
    private UserRole userRole;

    /**
     * Instantiates a new CustomUserDetails.
     *
     * @param userId   the user id
     * @param username the username
     * @param password the password
     * @param userRole the user role
     */
    public CustomUserDetails(long userId, String username, String password, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    /**
     * Gets userId.
     *
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Gets user role.
     *
     * @return the userRole
     */
    public UserRole getUserRole() {
        return userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_PREFIX.concat(userRole.name())));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}