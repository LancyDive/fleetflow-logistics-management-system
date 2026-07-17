package com.lancydive.fleetflow.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lancydive.fleetflow.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(
				new SimpleGrantedAuthority(
						"ROLE_"+user.getRole().name())
				);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

}
