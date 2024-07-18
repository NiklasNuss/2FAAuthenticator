package src;

import com.google.zxing.common.BitMatrix;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Controller {

    private static boolean logInSuccess = false;
    private static boolean twoFASuccess = false;
    private static BitMatrix qrCodeTOTP = null;
    private static BitMatrix qrCodeHOTP = null;
    private static String challengeValue = null;
    private static BitMatrix challengeQrCode = null;
    private static User decryptedUser = null;
    private static ArrayList<User> users = new ArrayList<>();

    protected static void start() {
        System.out.println("[INFO] Starting 2FA with OTP...");
        users = Storage.loadUsers();
        System.out.println("[INFO] Loaded " + users.size() + " users");
        Storage.loadMailer();
        System.out.println("[INFO] E-Mail configuration loaded");
    }

    protected static void stop() {
        System.out.println("[INFO] Exiting...");
        Storage.saveUsers(users);
        System.out.println("[INFO] Saved " + users.size() + " users");
        System.exit(0);
    }

    protected static void login(String username, String password) {

        // check if user exists
        if (!checkIfUserExists(username)) {
            System.out.println("[ERROR] User does not exist");
            GUI.displayError("User or password is wrong. Please try again.");
            return;
        }

        // load user
        decryptedUser = loadUser(username);

        // check if password is correct
        if (!checkPassword(password)) {
            System.out.println("[ERROR] Password is incorrect");
            GUI.displayError("User or password is wrong. Please try again.");
            reset();
            return;
        }

        // decrypt user
        decryptUser(password);
        logInSuccess = true;
        System.out.println("[INFO] User successfully logged in. Pending 2FA...");
    }

    private static boolean checkIfUserExists(String username) {

        // check if user exists in list, username is unique attribute
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("[INFO] User: " + username + " exists");
                return true;
            }
        }
        System.out.println("[INFO] User: " + username + " does not exist");
        return false;
    }

    private static User loadUser(String username) {

        // load user from list based on username
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("[INFO] User loaded: " + username);
                return user;
            }
        }
        System.out.println("[ERROR] User could not be loaded");
        return null;
    }

    private static void decryptUser(String password) {
        if (decryptedUser.getCrypto()) {
            decryptedUser = Crypto.decryptUser(decryptedUser, password);
        }
    }

    private static boolean checkPassword(String password) {
        if (decryptedUser.getCrypto()) {
            return Crypto.verifyPassword(password, decryptedUser.getPassword());
        } else {
            return password.equals(decryptedUser.getPassword());
        }
    }

    protected static void reset() {
        decryptedUser = null;
        twoFASuccess = false;
        logInSuccess = false;
        qrCodeTOTP = null;
        qrCodeHOTP = null;
        challengeValue = null;
        challengeQrCode = null;

        System.out.println("[INFO] Temporary data reset");
    }

    protected static boolean getLogInSuccess() {
        return logInSuccess;
    }

    protected static User getDecryptedUser() {
        return decryptedUser;
    }

    protected static void logout(String message) {
        decryptedUser.setMessage(message);
        if (decryptedUser.getCrypto()) {
            decryptedUser = Crypto.encryptUser(decryptedUser);
            System.out.println("[INFO] User data encrypted");
        }
        updateUsers(decryptedUser);
        System.out.println("[INFO] User data updated");
        reset();
        System.out.println("[INFO] User logged out");
    }

    private static void updateUsers(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                System.out.println("[INFO] User data updated");
                return;
            }
        }
    }

    protected static void verifyTOTP(String totp) {
        if (TwoFA.verifyTOTP(decryptedUser.getSecretKey(), totp)) {
            twoFASuccess = true;
            System.out.println("[INFO] 2FA successful");
        } else {
            twoFASuccess = false;
            System.out.println("[ERROR] 2FA failed");
            GUI.displayError("2FA failed. Please try again or go back.");
        }
    }

    protected static void verifyHOTP(String hotp) {
        if (TwoFA.verifyHOTP(decryptedUser.getSecretKey(), hotp, decryptedUser.getCounterLong())) {
            twoFASuccess = true;
            System.out.println("[INFO] 2FA successful");
            decryptedUser.incrementCounter();
            System.out.println("[INFO] Counter incremented");
            if (decryptedUser.getCrypto()) {
                decryptedUser = Crypto.encryptUser(decryptedUser);
                updateUsers(decryptedUser);
                System.out.println("[INFO] User data encrypted");
            }
            updateUsers(decryptedUser);
        } else {
            twoFASuccess = false;
            System.out.println("[ERROR] 2FA failed");
            GUI.displayError("2FA failed. Please try again or go back.");
        }
    }

    protected static boolean getTwoFASuccess() {
        return twoFASuccess;
    }

    protected static String getCounter() {
        System.out.println("[INFO] Counter: " + decryptedUser.getCounter());
        return decryptedUser.getCounter();
    }

    protected static void verifyOTPList(String userOtp) {
        if (TwoFA.verifyOTPList(decryptedUser.getOtpList(), userOtp)) {
            twoFASuccess = true;
            System.out.println("[INFO] 2FA successful");
            ArrayList<String> updatedOtpList = decryptedUser.getOtpList();
            updatedOtpList.remove(userOtp);
            decryptedUser.setOtpList(updatedOtpList);
            System.out.println("[INFO] OTP removed from list");
            if (decryptedUser.getCrypto()) {
                decryptedUser = Crypto.encryptUser(decryptedUser);
                updateUsers(decryptedUser);
                System.out.println("[INFO] User data encrypted");
            }
            updateUsers(decryptedUser);
            if (decryptedUser.getOtpList().isEmpty()) {
                decryptedUser.setOtpList(TwoFA.generateOTPList());
                if (decryptedUser.getCrypto()) {
                    decryptedUser = Crypto.encryptUser(decryptedUser);
                    updateUsers(decryptedUser);
                    System.out.println("[INFO] User data encrypted");
                }
                updateUsers(decryptedUser);
                GUI.displayInfo("All OTPs used. New OTP list generated.");
            }
        } else {
            twoFASuccess = false;
            System.out.println("[ERROR] 2FA failed");
            GUI.displayError("2FA failed. Please try again or go back.");
        }
    }

    protected static void verifyEmailOTP(String emailOtp) {
        if (Mailer.checkOTP(emailOtp)) {
            twoFASuccess = true;
            System.out.println("[INFO] 2FA successful");
        } else {
            twoFASuccess = false;
            System.out.println("[ERROR] 2FA failed");
            GUI.displayError("2FA failed. Please try again or go back.");
        }
    }

    protected static void sendEmailOTP() {
        Mailer.mailOTP(decryptedUser.getEmail());
        System.out.println("[INFO] E-Mail OTP sent");
    }

    protected static void register(String username, String password, String email, boolean crypto) {
        if (checkIfUserExists(username)) {
            System.out.println("[ERROR] User already exists");
            GUI.displayError("User already exists. Please try again.");
            return;
        }
        if (Mailer.checkEmail(email)) {
            System.out.println("[ERROR] Not a valid E-Mail address");
            GUI.displayError("Please Enter a valid E-Mail address");
            return;
        }
        User newUser = new User(username, password, email, crypto);
        System.out.println("[INFO] User: " + username + " created");

        // encrypt user and add to list, if crypto is enabled
        if (crypto) {
            newUser = Crypto.encryptUser(newUser);
            System.out.println("[INFO] User data encrypted");
        }
        users.add(newUser);

        // decrypt userData for SetupScreen
        if (crypto) {
            newUser = Crypto.decryptUser(newUser, password);
        }
        decryptedUser = newUser;

        System.out.println("[INFO] User successfully registered");

        // generate QR code for TOTP
        qrCodeTOTP = QrCode.generateQrCodeTOTP(decryptedUser.getSecretKey(), decryptedUser.getUsername());
        System.out.println("[INFO] TOTP QR code generated");

        // generate QR code for HOTP
        qrCodeHOTP = QrCode.generateQrCodeHOTP(decryptedUser.getSecretKey(), decryptedUser.getUsername());
        System.out.println("[INFO] HOTP QR code generated");

        GUI.setupScreen();
    }

    protected static void deleteUser() {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(decryptedUser.getUsername())) {
                users.remove(i);
                System.out.println("[INFO] User: " + decryptedUser.getUsername() + " deleted");
                GUI.displayInfo("User successfully deleted.");
                reset();
                return;
            }
        }
        System.out.println("[ERROR] User could not be deleted");
        GUI.displayError("User could not be deleted. Please try again.");
    }

    protected static BufferedImage getQrCodeTOTP() {
        int width = qrCodeTOTP.getWidth();
        int height = qrCodeTOTP.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (qrCodeTOTP.get(x, y) ? Color.BLACK : Color.WHITE).getRGB());
            }
        }
        return image;
    }

    protected static BufferedImage getQrCodeHOTP() {
        int width = qrCodeHOTP.getWidth();
        int height = qrCodeHOTP.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (qrCodeHOTP.get(x, y) ? Color.BLACK : Color.WHITE).getRGB());
            }
        }
        return image;
    }

    protected static BufferedImage getChallengeQrCode() {

        // generate challenge value
        challengeValue = TwoFA.generateChallengeValue();
        challengeQrCode = QrCode.createQRCode(TwoFA.generateChallengeString(decryptedUser.getUsername(), challengeValue));

        int width = challengeQrCode.getWidth();
        int height = challengeQrCode.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (challengeQrCode.get(x, y) ? Color.BLACK : Color.WHITE).getRGB());
            }
        }
        System.out.println("[INFO] Challenge QR code generated. Challenge value: " + challengeValue);
        return image;
    }

    protected static void verifyOCRA(String otp) {
        if (TwoFA.verifyOcraOtp(decryptedUser.getSecretKey(), challengeValue , otp)) {
            twoFASuccess = true;
            System.out.println("[INFO] 2FA successful");
        } else {
            twoFASuccess = false;
            System.out.println("[ERROR] 2FA failed");
            GUI.displayError("2FA failed. Please try again or go back.");
        }
    }
}
