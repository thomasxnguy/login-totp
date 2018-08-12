package com.example.auth.security.otp;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Custom WebAuthenticationDetails to handle 2FA security code
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String verificationCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        verificationCode = request.getParameter("code");
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}