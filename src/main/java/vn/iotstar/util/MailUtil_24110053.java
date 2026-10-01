package vn.iotstar.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public final class MailUtil_24110053 {
    private MailUtil_24110053() {}

    public static boolean sendOtp(String recipient, String otp) {
        String host = System.getenv("SMTP_HOST");
        String user = System.getenv("SMTP_USER");
        String password = System.getenv("SMTP_PASSWORD");
        String from = System.getenv("SMTP_FROM");
        if (host == null || user == null || password == null) {
            System.out.printf("[DEV OTP] %s -> %s%n", recipient, otp);
            return false;
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", System.getenv().getOrDefault("SMTP_PORT", "587"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from == null ? user : from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Ma OTP kich hoat tai khoan");
            message.setText("Ma OTP cua ban la: " + otp + ". Ma co hieu luc trong 5 phut.");
            Transport.send(message);
            return true;
        } catch (MessagingException exception) {
            throw new IllegalStateException("Không gửi được OTP qua email", exception);
        }
    }
}
