package com.API.User.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.API.User.Entity.User;
import com.API.User.Entity.UserRole;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		if(user.getBannedDate() == null) {
			return true;
		} else {
		LocalDateTime endBannedDate = LocalDateTime.parse(user.getEndBannedDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	    LocalDateTime now = LocalDateTime.now();
		return endBannedDate.isBefore(now);
	}
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		return !user.isDeleted();
	}
}

