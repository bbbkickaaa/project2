package com.API.User;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.API.User.Entity.User;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
 
    }

    @Override
    public String getPassword() {
        // 사용자 비밀번호 반환
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // 사용자 이름 반환
        return user.getUserid(); // 또는 user.getUsername() 등 사용자 ID를 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되었는지 여부 반환 (true: 만료되지 않음)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겼는지 여부 반환 (true: 잠기지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명이 만료되었는지 여부 반환 (true: 만료되지 않음)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 활성화되었는지 여부 반환 (true: 활성화됨)
        return true;
    }

}
