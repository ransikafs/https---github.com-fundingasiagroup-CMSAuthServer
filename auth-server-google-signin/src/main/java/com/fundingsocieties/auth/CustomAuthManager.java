package com.fundingsocieties.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthManager implements AuthenticationManager {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailAddress = authentication.getPrincipal().toString();
        if(!userDetailsManager.userExists(emailAddress)) {
            throw new UsernameNotFoundException("Account does not exist.");
        } else {
            UserDetails userDetails = userDetailsManager.loadUserByUsername(emailAddress);
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Account is disabled.");
            }
            return fromUserDetails(userDetails);
        }
    }

    private UsernamePasswordAuthenticationToken fromUserDetails(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
