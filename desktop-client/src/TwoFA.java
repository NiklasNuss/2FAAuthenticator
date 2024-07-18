package src;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;

public class TwoFA {

    protected static String generateSecretKey() {

        // generate 32 character secret key
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    private static byte[] convertToBytes(String secretKey) {
        Base32 base32 = new Base32();
        return base32.decode(secretKey);
    }

    private static byte[] timerToBytes(long time) {
        long timer = time / 1000L / 30L;        // Convert milliseconds to seconds, then divide by TOTP time step
        byte[] timerValue = new byte[8];
        for (int i = 7; i >= 0; i--) {
            timerValue[i] = (byte) (timer & 0xff);
            timer = timer >> 8;
        }
        return timerValue;
    }

    private static byte[] hash(byte[] secretKey, byte[] value) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "RAW");
            hmac.init(keySpec);
            return hmac.doFinal(value);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String generateTOTP(String secretKey, byte[] value) {
        byte[] result = hash(convertToBytes(secretKey), value);
        if (result == null) {
            throw new RuntimeException("Something went wrong: Could not produce OTP value");
        }
        int offset = result[result.length - 1] & 0xf;
        int binary = ((result[offset] & 0x7f) << 24)
                   | ((result[offset + 1] & 0xff) << 16)
                   | ((result[offset + 2] & 0xff) << 8)
                   | ((result[offset + 3] & 0xff));
        StringBuilder code = new StringBuilder(Integer.toString(binary % 1000000));
        while (code.length() < 6) code.insert(0, "0");
        return code.toString();
    }

    protected static String getTOTP(String secretKey) {
        long time = System.currentTimeMillis();
        byte[] timerValue = timerToBytes(time);
        return generateTOTP(secretKey, timerValue);
    }

    private static String futureTOTP(String secretKey) {
        long time = System.currentTimeMillis();
        byte[] timerValue = timerToBytes(time + 30000);
        return generateTOTP(secretKey, timerValue);
    }

    private static String pastTOTP(String secretKey) {
        long time = System.currentTimeMillis();
        byte[] timerValue = timerToBytes(time - 30000);
        return generateTOTP(secretKey, timerValue);
    }

    protected static boolean verifyTOTP(String secretKey, String totp) {
        return totp.equals(getTOTP(secretKey))
            || totp.equals(futureTOTP(secretKey))
            || totp.equals(pastTOTP(secretKey));
    }

    protected static ArrayList<String> generateOTPList() {
        ArrayList<String> otpList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            otpList.add(getTOTP(generateSecretKey()));
        }
        return otpList;
    }

    protected static boolean verifyOTPList(ArrayList<String> otpList, String userOtp) {
        for (String otp : otpList) {
            if (otp.equals(userOtp)) {
                return true;
            }
        }
        return false;
    }

    private static String generateHOTP(String secretKey, long counter) {
        byte[] counterValue = new byte[8];
        for (int i = 7; i >= 0; i--) {
            counterValue[i] = (byte) (counter & 0xff);
            counter = counter >> 8;
        }
        return generateTOTP(secretKey, counterValue);
    }

    protected static boolean verifyHOTP(String secretKey, String hotp, long counter) {
        return hotp.equals(generateHOTP(secretKey, counter));
    }

    protected static String generateChallengeValue() {

        // generate 32 character challenge value, only numbers
        SecureRandom random = new SecureRandom();
        StringBuilder challengeValue = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            challengeValue.append(random.nextInt(10));
        }
        return challengeValue.toString();
    }

    protected static String generateChallengeString(String username, String value) {

        //challenge link made up of username, challengeValue and a time stamp in hh:mm:ss format
        return "otpauth://ocra/" + username + "+" + value + "+" + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    protected static boolean verifyOcraOtp(String secretKey, String challengeValue, String otp) {
        byte[] challenge = convertToBytes(challengeValue);
        return otp.equals(generateTOTP(secretKey, challenge));
    }
}
