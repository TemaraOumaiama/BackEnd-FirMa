package com.app.service;

import com.app.constant.EmailConstant;
import com.app.modele.Document;
import com.app.repository.DocumentRepository;
import com.app.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final DepartementService departementService;
    private final CategorieService categorieService;
    private final UserService userService;
    private final DocumentRepository documentRepository;

    @Autowired
    public MailService(JavaMailSender javaMailSender, DepartementService departementService,
                       CategorieService categorieService, UserService userService, DocumentRepository documentRepository) {
        this.javaMailSender = javaMailSender;
        this.categorieService = categorieService;
        this.departementService = departementService;
        this.userService = userService;
        this.documentRepository = documentRepository;
    }

    public void sendEmail(String recipient, String content,String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setFrom(EmailConstant.FROM_EMAIL);
        message.setCc(EmailConstant.CC_EMAIL);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    public void sendEmailWithAttachment(String recipient, String content, byte[] attachmentContent, String attachmentFileName) {
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(recipient);
            messageHelper.setFrom(EmailConstant.FROM_EMAIL);
            messageHelper.setCc(EmailConstant.CC_EMAIL);
            messageHelper.setSubject(EmailConstant.EMAIL_SUBJECT);
            messageHelper.setText(content);

            // Attachement du document
            DataSource attachment = new ByteArrayDataSource(attachmentContent, "application/octet-stream");
            messageHelper.addAttachment(attachmentFileName, attachment);
        });
    }

    public void fetchUnreadDocuments() {
        try {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            Properties properties = mailSender.getJavaMailProperties();

            Session session = Session.getInstance(properties);
            Store store = session.getStore(EmailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL);
            store.connect(EmailConstant.GMAIL_SMTP_SERVER, EmailConstant.USERNAME, EmailConstant.PASSWORD);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                        saveAttachment(bodyPart);
                    }
                }
                message.setFlag(Flags.Flag.SEEN, true);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            // Gérer les exceptions lors de la récupération des messages
        }
    }

    private Document saveAttachment(BodyPart bodyPart) throws IOException, MessagingException {
        String fileName = bodyPart.getFileName();
        if (fileName != null && !fileName.isEmpty()) {
            try (InputStream inputStream = bodyPart.getInputStream()) {
                byte[] content = IOUtils.toByteArray(inputStream);
                Document document = new Document();
                String name = fileName + LocalDateTime.now();
                document.setNom(name);
                long id = 2l;
                document.setCreatedBy(userService.findByUserId(id));
                document.setModifiedBy(userService.findByUserId(id));
                document.setCategorie(categorieService.findById(1l));
                document.setDepartement(departementService.findById(5l));
                document.setContent(content);
                document.setDateCreation(LocalDateTime.now());
                document.setDateModification(LocalDateTime.now());

                return documentRepository.save(document);
            }
        }
        return null;
    }
}