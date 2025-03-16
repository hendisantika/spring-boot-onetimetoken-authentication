package id.my.hendisantika.onetimetokenauthentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-onetimetoken-authentication
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 16/03/25
 * Time: 11.06
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Component
public class OttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
    private final EmailService emailService;

    public OttSuccessHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .path("/login/ott")
                .queryParam("token", oneTimeToken.getTokenValue());

        String magicLink = builder.toUriString();

        // send email (JavaMail, SendGrid) or SMS
        log.info("Magic Link: {}", magicLink);

        var body = """
                Hello, from Spring Security! 
                Below you will find your magic link to login to our super secret application!
                
                %s
                """.formatted(magicLink);

        var sendTo = oneTimeToken.getUsername();
        log.info("Sending One Time Token to username: {}", sendTo);
        emailService.sendEmail(
                getEmail(sendTo),
                "One Time Token Login",
                body
        );

        this.redirectHandler.handle(request, response, oneTimeToken);
    }

    private String getEmail(String username) {
        // this would be a database lookup for username
        log.info("Retrieving email for user: {}", username);
        return "yuji@yopmail.com";
    }
}
