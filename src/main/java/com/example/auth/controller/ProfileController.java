package com.example.auth.controller;

import com.example.auth.repository.UserTOTPRepository;
import com.example.auth.repository.jpa.UserTOTP;
import com.example.auth.totp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;

@RequestMapping("/profile")
public class ProfileController {

    private OTPService otpService;

    private UserTOTPRepository userTOTPRepository;

    @Autowired
    public ProfileController(OTPService otpService, UserTOTPRepository userTOTPRepository) {
        this.otpService = otpService;
        this.userTOTPRepository = userTOTPRepository;
    }

    @RequestMapping("/2FA")
    public String profile2FA(Model model, Principal principal) {
        String userName = principal.getName();

        UserTOTP userTOTP = userTOTPRepository.findOne(userName);
        if (StringUtils.isEmpty(userTOTP.getKey())) {
            String secretKey = otpService.generateSecretKey();
            String path = "/static/qr."+userName;
            try {
                otpService.createQRCode(secretKey, path, 20, 20);
                model.addAttribute("qr", path);
            } catch (Exception e) {
                //Handle Exception
            }
        }
        return "totp";
    }

}
