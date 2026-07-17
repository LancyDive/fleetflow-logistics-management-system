package com.lancydive.fleetflow.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User exsitingUser = userRepository.findByEmail(username)
							.orElseThrow( ()-> 
									new UsernameNotFoundException("User Not Found"));
		return new CustomUserDetails(exsitingUser);
	}
	
	
}
