package src;

import java.util.ArrayList;

public class User {

    private final String username;      // Username for account, has to be unique
    private final String password;      // Password for account and encryption
    private final String email;         // Email for email OTP, checked to be valid
    private final String secretKey;     // Secret key for OTP generation
    private final boolean crypto;       // boolean for encryption
    private ArrayList<String> otpList;  // OTP List for Tan List, 10 codes
    private String message;             // Secret message for encryption
    private String counter;             // counter for HOTP, starts at 1 to match

    protected User(String username, String password, String email, boolean crypto) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.otpList = TwoFA.generateOTPList();
        this.secretKey = TwoFA.generateSecretKey();
        this.counter = "1";
        this.message = "Default Secret Message.";
        this.crypto = crypto;
    }

    protected User(String username, String password, String secretKey, ArrayList<String> otpList, String message, String counter, String email, boolean crypto) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.secretKey = secretKey;
        this.otpList = otpList;
        this.message = message;
        this.counter = counter;
        this.crypto = crypto;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected String getSecretKey() {
        return secretKey;
    }

    protected ArrayList<String> getOtpList() {
        return otpList;
    }

    protected void setOtpList(ArrayList<String> otpList) {
        this.otpList = otpList;
    }

    protected String getEmail() {
        return email;
    }

    protected String getCounter() {
        return counter;
    }

    protected long getCounterLong() {
        return Long.parseLong(counter);
    }

    protected void incrementCounter() {
        long newCounter = Long.parseLong(counter) + 1;
        counter = String.valueOf(newCounter);
    }

    protected boolean getCrypto() {
        return crypto;
    }
}
