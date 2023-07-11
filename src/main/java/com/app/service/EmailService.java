package com.app.service;

import com.sun.mail.smtp.SMTPTransport;
import javax.mail.Transport;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.app.constant.EmailConstant.*;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {

    public void sendNewPasswordEmail(String nom, String prenom, String password, String email) throws MessagingException {
        Message message = createEmail(nom, prenom, password, email);
        Transport transport = null;

        try {
            transport = getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);

            if (transport instanceof SMTPTransport smtpTransport) {
                smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
                smtpTransport.sendMessage(message, message.getAllRecipients());
                smtpTransport.close();
            } else {
                throw new MessagingException("Unable to cast Transport to SMTPTransport");
            }
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }


    public void sendNewPasswordEmailNewUser(String nom, String prenom, String password, String email) throws MessagingException {
        Message message = createEmailNew(nom, prenom, password, email);
        Transport transport = null;

        try {
            transport = getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);

            if (transport instanceof SMTPTransport smtpTransport) {
                smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
                smtpTransport.sendMessage(message, message.getAllRecipients());
                smtpTransport.close();
            } else {
                throw new MessagingException("Unable to cast Transport to SMTPTransport");
            }
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }


    private Message createEmail(String nom,String prenom, String password, String email) throws MessagingException {
        String nomUpp = nom.substring(0, 1).toUpperCase() + nom.substring(1).toLowerCase();
        String prenomUpp = prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase();
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Bonjour " + nomUpp+" " +prenomUpp + ", \n \n Votre nouveau mot de passe: " + password + "\n \n The Support Team DOCMA");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }



    private Message createEmailNew(String nom,String prenom, String password, String email) throws MessagingException {
        String nomUpp = nom.substring(0, 1).toUpperCase() + nom.substring(1).toLowerCase();
        String prenomUpp = prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase();
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("DocMA- Nouvelle inscription");
        message.setText
                ("Bonjour " + nomUpp+" " +prenomUpp + ", \n \n" +
                "Bienvenue dans notre application ! Nous sommes ravis de vous accueillir en tant que nouvel utilisateur.\n" +
                "Voici vos informations de connexion: \n"+
                "         Nom d'utilisateur: " + nom.toLowerCase()+prenom.toLowerCase() +
                "\n        Mot de passe: " + password +
                "\nNous vous recommandons de garder ces informations en lieu sûr et de ne pas les partager avec d'autres personnes.\n" +
                        "Si vous rencontrez des problèmes lors de votre connexion ou si vous avez des questions, n'hésitez pas à nous contacter à testdocma@gmail.com. \n\nNous espérons que vous apprécierez l'utilisation de notre application et que vous en tirerez le meilleur parti ! \n \n \n Cordialement,\nEquipe de support DOCMA");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }






    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}
