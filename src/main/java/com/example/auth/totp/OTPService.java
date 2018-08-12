package com.example.auth.totp;

import com.google.zxing.WriterException;

import java.io.IOException;

/**
 * This is the interface for the generation of OTP Service.
 */
public interface OTPService {

    /**
     * Generate a random secret key for the generation of OTP
     * @return : the secret key
     */
    String generateSecretKey();

    /**
     * Generate a time-based OTP as a variable number of digit
     * @param secretKey : the secret key
     * @paran digit : length of the totp
     * @return : the time-based one time password
     */
    String getTOTP(String secretKey, int digit);

    /**
     * Generate a QR code based on the secret key and user account
     * @param secretKey : the secret key
     * @param account : the username
     * @param filePath : output directory to store the QR
     * @param height : QR's height
     * @param width : QR's width
     */
    void createQRCode(String secretKey, String account, String filePath, int height, int width) throws WriterException, IOException;
}
