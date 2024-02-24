package com.API.User.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.API.User.Entity.User;
import com.API.User.Entity.UserRole;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        UserRole role = user.getRole();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role.getTitle()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserid();
    }
    

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되지 않았는지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨 있지 않은지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 사용자의 자격 증명(비밀번호)이 만료되지 않았는지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자 계정이 활성화되어 있는지 여부를 반환합니다.
        return true;
    }
}

