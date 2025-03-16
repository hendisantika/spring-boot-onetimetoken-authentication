package id.my.hendisantika.onetimetokenauthentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-onetimetoken-authentication
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 16/03/25
 * Time: 11.07
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        log.info("home {}", LocalDateTime.now());
        return "index";
    }

    @GetMapping("/ott/sent")
    public String ottSent() {
        log.info("sent {}", LocalDateTime.now());
        return "sent";
    }
}
