package src;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class QrCode {

    private static final String issuer = "2FA with OTPs";

    private static String generateQrCodeLinkTOTP(String secretKey, String account) {

        // generate code link for TOTP with secretKey and Account name based on Google authenticator format
        return "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret="
                + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer="
                + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String generateQrCodeLinkHOTP(String secretKey, String account) {

        // generate code link for HOTP with secretKey and Account name based on Google authenticator format
        return "otpauth://hotp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret="
                + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer="
                + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
    }

    protected static BitMatrix createQRCode(String data) {

        // create QR code with data, return as BitMatrix
        try {
            return new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 250, 250);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    protected static BitMatrix generateQrCodeTOTP(String secretKey, String account) {
        return createQRCode(generateQrCodeLinkTOTP(secretKey, account));
    }

    protected static BitMatrix generateQrCodeHOTP(String secretKey, String account) {
        return createQRCode(generateQrCodeLinkHOTP(secretKey, account));
    }
}
