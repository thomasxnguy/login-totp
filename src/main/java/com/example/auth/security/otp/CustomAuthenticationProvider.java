package com.example.auth.security.otp;

import com.example.auth.repository.UserTOTPRepository;
import com.example.auth.repository.jpa.UserTOTP;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserTOTPRepository userTOTPRepository;

    @Override
    public Authentication authenticate(Authentication auth) {
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationCode();
        Optional<UserTOTP> user = userTOTPRepository.findById(auth.getName());
        if (!user.isPresent()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (user.get().isEnabled()) {
            Totp totp = new Totp(user.get().getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verfication code");
            }
        }

        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
