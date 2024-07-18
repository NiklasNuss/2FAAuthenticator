package src;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

    private static String OTP;
    private static String username;
    private static String password;
    private static String smtpServer;
    private static String port;
    private static String smtpAuth;
    private static String starttls;
    private static long time;

    protected static void mailOTP(String email) {
        time = System.currentTimeMillis();
        OTP = TwoFA.getTOTP(TwoFA.generateSecretKey());
        sendMail(email);
        System.out.println("[INFO] OTP code valid for 10min send to " + email);
    }

    private static void sendMail(String email) {

        // initialize mail configuration with values from email.conf
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpServer);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", smtpAuth);
        prop.put("mail.smtp.starttls.enable", starttls);

        Session session =
                Session.getInstance(
                        prop,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("OTP code");
            message.setText("OTP code: " + OTP + "\n\n" + "Valid for 10min");

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("[ERROR] Error while sending email, check email.conf file");
            e.printStackTrace();
        }
    }

    protected static boolean checkOTP(String otp) {
        return otp.equals(OTP) && System.currentTimeMillis() - time < 600000;
    }

    protected static boolean checkEmail(String email) {
        return !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    protected static void setUsername(String name) {
        username = name;
    }

    protected static void setPassword(String pw) {
        password = pw;
    }

    protected static void setSmtpServer(String server) {
        smtpServer = server;
    }

    protected static void setPort(String p) {
        port = p;
    }

    protected static void setSmtpAuth(String auth) {
        smtpAuth = auth;
    }

    protected static void setStarttls(String tls) {
        starttls = tls;
    }
}
