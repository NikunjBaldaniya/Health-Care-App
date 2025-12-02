package com.example.healthcareapp.Model;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

public class MailSender {

    // Send Mail Method
    public static void sendMail(Context context, final String userEmail, final String otp) {

        // Your Gmail and Password
        final String username = "<PROVIDE_HERE_YOUR_EMAIL>"; // Use Email Where GET THE OTP
        // To Get Password from Google Follow The Steps :
        // Google >> Manage Account >> Search >> App Password >> Provides App Name and Click On Create and get the password
        final String password = "<PROVIDE_PASSWORD_OF_YOUR_EMAIL>"; //Which Gives By Google Services
        /* NOTE : For No Idea You Have So Go On YouTube and Simple Search This
                    - "How to generate app password for Gmail SMTP"
                    - "Gmail SMTP app password setup step by step"
                    - "Or other related to this topic"
        */
        // Run in background thread (avoid blocking main UI thread)
        new Thread(() -> {
            try {
                // SMTP properties (Gmail server details)
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true"); // Enable authentication
                props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
                props.put("mail.smtp.host", "smtp.gmail.com"); // Gmail host
                props.put("mail.smtp.port", "587"); // Gmail TLS port

                // Gmail session with authentication
                Session session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                // Create Email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username)); // From
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail)); // To
                message.setSubject("Your OTP Code"); // Subject

                // Professional OTP message body
                String otpMessage =
                        "Dear User,\n\n" +
                                "Your One-Time Password (OTP) for verification is:\n\n" +
                                otp + "\n\n" +
                                "Please enter this code within 10 minutes to proceed.\n\n" +
                                "If you did not request this, please ignore this email.\n\n" +
                                "Best Regards,\n" +
                                "We Health Care Team";

                message.setText(otpMessage); // Set body text
                // Send mail
                Transport.send(message);
            } catch (MessagingException e) {
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Failed to send OTP!" + e, Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    // Generate 4 digit OTP
    public static String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Random 1000–9999
        return String.valueOf(otp);
    }
}
