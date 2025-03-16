package id.my.hendisantika.onetimetokenauthentication;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-onetimetoken-authentication
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 16/03/25
 * Time: 11.05
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
//    public void sendEmail(String to, String subject, String content) throws IOException {
//        Email from = new Email("yuji@yopmail.com");
//        Email toEmail = new Email(to);
//        Content emailContent = new Content("text/plain", content);
//        Mail mail = new Mail(from, subject, toEmail, emailContent);
//
//        // configure SendGrid client
//        SendGrid sg = new SendGrid(sendgridApiKey);
//        Request request = new Request();
//
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//
//            Response response = sg.api(request);
//
//            System.out.println("Status Code: " + response.getStatusCode());
//            System.out.println("Response Body " + response.getBody());
//            System.out.println("Response Headers " + response.getHeaders());
//        } catch (IOException ex) {
//            throw new IOException("Failed to send email: " + ex.getMessage());
//        }
//    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setFrom("no-reply@jvm.my.id");
            helper.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
