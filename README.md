# Login Page with Spring
Simple login page using Spring Boot, Spring Data, Spring Security, Spring MVC

Add 2FactorAuthentication following OTP specification
https://tools.ietf.org/html/rfc6238

## Demo

This application used H2, an in-memory database for storing persistent data. We have two pre-defined user : 
 1) admin
 2) login (normal user)

#### Step1

Access http://localhost:8080 and authenticate using (login/password)

#### Step2

Clik on "My Profile" page

#### Step3

Clik on "Activate TOTP" 

#### Step4
Run the `google-authenticator` and scan the following QR Code
<img src="https://github.com/thomasxnguy/login-totp/blob/master/img/QR_img.png">
