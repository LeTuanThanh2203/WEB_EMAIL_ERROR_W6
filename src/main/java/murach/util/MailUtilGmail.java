package murach.util;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailUtilGmail {

    public static void sendMail(
            String to,
            String from,
            String subject,
            String body,
            boolean bodyIsHTML) throws MessagingException {

        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);
        session.setDebug(true);

        Message message = new MimeMessage(session);

        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);

        message.setFrom(fromAddress);

        message.setRecipient(
                Message.RecipientType.TO,
                toAddress
        );

        String username = System.getenv("GMAIL_USERNAME");
        String password = System.getenv("GMAIL_APP_PASSWORD");

        Transport transport = session.getTransport();

        transport.connect(
                username,
                password
        );

        transport.sendMessage(
                message,
                message.getAllRecipients()
        );

        transport.close();
    }
}