package src;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public class Crypto {

    private static String generateSalt() {
        return BCrypt.gensalt();
    }

    private static String hashPw(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    protected static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private static byte[] hash(String password) {

        // generate 32 character secret key from password to use as key for AES encryption
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encrypt(String txt, String password) {
        try {
            byte[] key = hash(password);
            if (key == null) {
                throw new RuntimeException("[ERROR] Unable to hash password");
            }
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedMessageBytes = cipher.doFinal(txt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedMessageBytes);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] encryption failed: " + e);
        }
    }

    private static String decrypt(String encryptedTxt, String password) {
        try {
            byte[] key = hash(password);
            if (key == null) {
                throw new RuntimeException("[ERROR] Unable to hash password");
            }
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedMessageBytes = Base64.getDecoder().decode(encryptedTxt);
            byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes);
            return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] decryption failed: " + e);
        }
    }

    private static ArrayList<String> encrypt(ArrayList<String> list, String password) {
        ArrayList<String> encryptedOtpList = new ArrayList<>();
        for (String otp : list) {
            encryptedOtpList.add(encrypt(otp, password));
        }
        return encryptedOtpList;
    }

    private static ArrayList<String> decrypt(ArrayList<String> list, String password) {
        ArrayList<String> decryptedOtpList = new ArrayList<>();
        for (String otp : list) {
            decryptedOtpList.add(decrypt(otp, password));
        }
        return decryptedOtpList;
    }

    protected static User encryptUser(User user) {
        return new User(
                user.getUsername(),
                hashPw(user.getPassword(), generateSalt()),
                encrypt(user.getSecretKey(), user.getPassword()),
                encrypt(user.getOtpList(), user.getPassword()),
                encrypt(user.getMessage(), user.getPassword()),
                encrypt(String.valueOf(user.getCounter()), user.getPassword()),
                encrypt(user.getEmail(), user.getPassword()),
                user.getCrypto());
    }

    protected static User decryptUser(User user, String password) {
        return new User(
                user.getUsername(),
                password,
                decrypt(user.getSecretKey(), password),
                decrypt(user.getOtpList(), password),
                decrypt(user.getMessage(), password),
                decrypt(String.valueOf(user.getCounter()), password),
                decrypt(user.getEmail(), password),
                user.getCrypto());
    }
}
