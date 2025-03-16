package id.my.hendisantika.onetimetokenauthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;

    public void sendEmail(String to, String subject, String content) throws IOException {
        Email from = new Email("danvega@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        // configure SendGrid client
        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body " + response.getBody());
            System.out.println("Response Headers " + response.getHeaders());
        } catch (IOException ex) {
            throw new IOException("Failed to send email: " + ex.getMessage());
        }
    }
}
