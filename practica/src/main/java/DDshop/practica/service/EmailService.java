package DDshop.practica.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    public void sendMail(String toEmail, String subject, String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("andreiraul.rusu@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        sender.send(message);
    }

    public void sendHtmlMail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("andreiraul.rusu@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            sender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea emailului HTML: " + e.getMessage());
        }
    }
}
