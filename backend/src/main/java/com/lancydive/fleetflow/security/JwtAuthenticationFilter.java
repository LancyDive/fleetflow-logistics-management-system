package com.lancydive.fleetflow.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		
		System.out.println("JWT FILTER: " 
		        + request.getMethod() 
		        + " " 
		        + request.getRequestURI());

		  // No JWT provided
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);// Each filter gets a chance to inspect or modify the request.When a
													// filter finishes its work, it says:"I'm done. Pass the request to
													// the next filter."
			return;
		}

		String jwt = authHeader.substring(7);
		try {
		String username = jwtService.extractUsername(jwt);

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

		if (jwtService.isTokenValid(jwt, userDetails)&& SecurityContextHolder.getContext().getAuthentication()== null) {
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails,
					null, // psssword not needed as we use jwt and security purpose
					userDetails.getAuthorities());
			
			authentication.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request));//This stores information like:Client IP, Session ID (if applicable)
			
			SecurityContextHolder.getContext().setAuthentication(authentication);// "Spring, for the rest of this
		}																			// request, this is the
																					// authenticated user."
		} catch (JwtException | UsernameNotFoundException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                    """
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Invalid or expired JWT token"
                    }
                    """
            );

            return;
        }
		
		filterChain.doFilter(request, response);
	}

}
