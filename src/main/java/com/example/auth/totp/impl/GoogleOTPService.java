package com.example.auth.totp.impl;

import com.example.auth.totp.OTPService;
import com.example.auth.totp.TOTP;
import com.google.zxing.WriterException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Service
public class GoogleOTPService implements OTPService {
    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        // make the secret key more human-readable by lower-casing and
        // inserting spaces between each group of 4 characters
        return secretKey.toLowerCase().replaceAll("(.{4})(?=.{4})", "$1 ");
    }

    public String getTOTP(String secretKey, int digit) {
        String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(normalizedBase32Key);
        String hexKey = Hex.encodeHexString(bytes);
        long time = (System.currentTimeMillis() / 1000) / 30;
        String hexTime = Long.toHexString(time);
        return TOTP.generateTOTP(hexKey, hexTime, Integer.toString(digit));
    }

    private String getGoogleAuthenticatorBarCode(String secretKey, String account) {
        String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
        try {
            return QR_PREFIX + "otpauth://totp/"
                    + URLEncoder.encode(TOTP.ISSUER + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(normalizedBase32Key, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(TOTP.ISSUER, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String createQRCode(String secretKey, String account, String filePath, int height, int width)
            throws WriterException, IOException {
        String totpUrl = getGoogleAuthenticatorBarCode(secretKey, account);
        /*
        BitMatrix matrix = new MultiFormatWriter().encode(totpUrl, BarcodeFormat.QR_CODE,
                width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }*/
        return totpUrl;
    }

}
