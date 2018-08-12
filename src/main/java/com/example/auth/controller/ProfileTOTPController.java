package com.example.auth.controller;

import com.example.auth.repository.UserTOTPRepository;
import com.example.auth.repository.jpa.UserTOTP;
import com.example.auth.totp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("/profile")
public class ProfileTOTPController {

    private OTPService otpService;

    private UserTOTPRepository userTOTPRepository;

    @Autowired
    public ProfileTOTPController(OTPService otpService, UserTOTPRepository userTOTPRepository) {
        this.otpService = otpService;
        this.userTOTPRepository = userTOTPRepository;
    }

    @RequestMapping("/2FA")
    public String profile2FA(Model model, Principal principal) {
        String userName = principal.getName();

        Optional<UserTOTP> userTOTP = userTOTPRepository.findById(userName);
        if (!userTOTP.isPresent()) {
            String secretKey = otpService.generateSecretKey();
            String path = "/static/qr."+userName;
            try {
                String qrUrl = otpService.createQRCode(secretKey, userName, path, 20, 20);
                model.addAttribute("qr", qrUrl);
                System.out.println();
            } catch (Exception e) {
                //Handle Exception
            }
        }
        return "totp";
    }

}
