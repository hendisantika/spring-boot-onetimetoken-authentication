package id.my.hendisantika.onetimetokenauthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
