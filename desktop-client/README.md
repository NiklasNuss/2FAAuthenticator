![icon.png](icon.png)

# 2FA-Server

Simple Application to store a secret message for a given user. 
User can access the message after logging in.

Build with OPENJDK 22.0.1 in IntelliJ IDEA.

## Features v1.1.2

- simple User management: register, login, logout, delete
- User registration with a username, email and password
- optional data encryption 
- Setup screen for 2FA with TOTP/HOTP QR codes, secret key and OTP List
- User can access the secret message after logging in
- Support for different 2FA methods
  - TOTP
  - HOTP
  - OTP List
  - Email OTP
  - OCRA 

## OTP Format

- TOTP:
  - secret key is a 32 character String
  - 6-digit code
  - based on unix time
  - 30 seconds interval 
  - SHA1
  - QR code format based on Google Authenticator
    - Example: otpauth://totp/2FA%20with%20OTPs%3Atest1234?secret=UJ7I6ISMTPRAVSD4M3NIBMYMBBT3DV5U&issuer=2FA%20with%20OTPs
- HOTP:
  - secret key is a 32 character String
  - 6-digit code
  - counter based, starting at 1 to match Google Authenticator
  - SHA1
  - QR code format based on Google Authenticator
    - Example: otpauth://hotp/2FA%20with%20OTPs%3Atest1234?secret=UJ7I6ISMTPRAVSD4M3NIBMYMBBT3DV5U&issuer=2FA%20with%20OTPs
- OTP List:
  - 10 one time use 6-digit codes
  - generated as TOTP with a random secret key
  - new codes are generated after all codes are used
- Email OTP:
  - 6-digit code
  - generated with a random secret key
  - sent to the user's email
  - code is valid for 10 minutes
- OCRA:
  - secret key is a 32 character String
  - 6-digit code as Response
  - 32-digit challengeValue
  - Response is generated with the secret key and the challengeValue as a HOTP, where the counter is the challengeValue
  - QR code format: user+challengeValue+time
    - Example: otpauth://ocra/username+44783583734032726237701257030483+14:27:42

## Run latest release

1. Make sure at least OPENJDK 22.0.1 is installed and setup
2. download latest release
3. extract the zip file
4. Setup email.conf file with your email credentials
5. run the jar file with `java -jar 2FA-Server.jar`

## Setup email.conf

Email was tested with smtp.gmail.com. Other endpoints might not work or need modifications.

email.conf is a simple text file with six lines. Replace the placeholders with your email credentials:

```
username
AppPassword
smtp.gmail.com
587
true
true
```

## Used Libraries

- commons-codec:commons-codec:1.17.0 for Base32 encoding
- io.mosip.kernel:kernel-qrcodegenerator-zxing:1.2.0.1 for QR code generation
- rg.mindrot:jbcrypt:0.4 for password hashing with bcrypt
- com.sun.mail:javax.mail:1.6.2 for email sending
- com.google.code.gson:gson:2.10.1 for JSON generation and parsing

The libraries are included in the jar file. 
Need to be manually added to the project, when using the source code.