package com.example.auth.totp;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface OTPService {

    String generateSecretKey();

    String getTOTP(String secretKey);

    void createQRCode(String secretKey, String account, String filePath, int height, int width) throws WriterException, IOException;
}
