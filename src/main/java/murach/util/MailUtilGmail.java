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

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", "465");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getDefaultInstance(props);
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

        Transport transport = session.getTransport();

        transport.connect(
                "tuanthanhlvq413@gmail.com",
                "wwlladyleowixzel"
        );

        transport.sendMessage(
                message,
                message.getAllRecipients()
        );

        transport.close();
    }
}