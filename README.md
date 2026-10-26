# Spring Security One-Time Token Authentication

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-onetimetoken-authentication/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-onetimetoken-authentication/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.x-6DB33F?logo=springboot)

Enhance your application's security with passwordless authentication using Spring Security's One-Time Token (OTT)
feature. This project demonstrates how to implement magic link authentication, allowing users to securely log in through
email-delivered tokens.

## Overview

This Spring Boot application showcases a modern approach to authentication by implementing magic links through Spring
Security's OTT functionality. When users attempt to log in, they receive a secure one-time token via email that grants
them temporary access to the application.

## Tech Stack

| Component        | Version                                                        |
|------------------|----------------------------------------------------------------|
| Java             | 25                                                             |
| Spring Boot      | 4.1.x (Spring Framework 7, Spring Security 7)                  |
| Build tool       | Maven (wrapper included)                                       |
| Template engine  | [JTE](https://jte.gg) 3.2.x with `jte-spring-boot-starter-4`   |
| Email            | Spring Boot Starter Mail (`JavaMailSender`) over SMTP (Brevo)  |

## Project Requirements

- JDK 25
- An SMTP account for email delivery (defaults point to [Brevo](https://www.brevo.com/) SMTP relay)

## Dependencies

The project relies on the following key dependencies:

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Mail
- Spring Boot Starter Actuator
- Spring Boot DevTools
- Lombok
- JTE (Java Template Engine) + `jte-spring-boot-starter-4` + `jte-maven-plugin`
- Spring Boot Starter Test

## Getting Started

1. Configure SMTP credentials through environment variables (see `src/main/resources/application.properties`):

   | Variable        | Default                 |
   |-----------------|-------------------------|
   | `MAIL_HOST`     | `smtp-relay.brevo.com`  |
   | `MAIL_PORT`     | `587`                   |
   | `MAIL_USERNAME` | `YOUR_USERNAME`         |
   | `MAIL_PASSWORD` | `YOUR_PASSWORD`         |
   | `MAIL_FROM`     | `no-reply@jvm.my.id`    |

   ```bash
   export MAIL_USERNAME=your-smtp-username
   export MAIL_PASSWORD=your-smtp-password
   ```

2. Update the recipient lookup in `OttSuccessHandler.getEmail(...)` (it returns a hard-coded address; in a real
   application this would be a database lookup by username).

3. Update user configuration in `SecurityConfig.java` as needed:
   ```java
   var yuji = User.withUsername("yu71").password("{noop}53cret").build();
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Then open http://localhost:8080, request a one-time token for user `yu71`, and follow the magic link from the email
   (the link is also logged to the console).

## How It Works

### Authentication Flow

1. User requests access to a protected resource
2. System generates a one-time token
3. Token is sent via email using `JavaMailSender`
4. User clicks the magic link containing the token
5. Spring Security validates the token and grants access

### Key Components

#### Security Configuration

The `SecurityConfig` class sets up the security filter chain and configures authentication:

```java

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/ott/sent").permitAll()
                    .requestMatchers("/login/ott").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .oneTimeTokenLogin(Customizer.withDefaults())
            .build();
}
```

#### OTT Success Handler

The `OttSuccessHandler` manages token generation and email delivery. Note that Spring Framework 7 removed
`UriComponentsBuilder.fromHttpUrl(...)`, so the magic link is built with `fromUriString(...)`:

```java

@Override
public void handle(HttpServletRequest request, HttpServletResponse response,
                   OneTimeToken oneTimeToken) throws IOException, ServletException {
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(UrlUtils.buildFullRequestUrl(request))
            .replacePath(request.getContextPath())
            .replaceQuery(null)
            .fragment(null)
            .path("/login/ott")
            .queryParam("token", oneTimeToken.getTokenValue());

    String magicLink = builder.toUriString();
    // Send magic link via email
}
```

## Frontend Templates

The application includes two JTE templates in `src/main/jte`, precompiled by the `jte-maven-plugin` during the build:

- `index.jte`: Main application page (protected)
- `sent.jte`: Confirmation page for token delivery

## Testing

Run the included tests with the Maven wrapper:

```bash
./mvnw test
```

## Continuous Integration & Dependency Updates

- **GitHub Actions** (`.github/workflows/maven.yml`) builds and tests the project with Temurin JDK 25 on every push and
  pull request to `main`, and submits the Maven dependency graph to GitHub on pushes to `main`.
- **Dependabot** (`.github/dependabot.yml`) checks Maven and GitHub Actions dependencies daily at 05:00 (Asia/Jakarta):
    - Spring Boot, JTE, and other minor/patch Maven updates are grouped into separate PRs
    - All GitHub Actions updates are grouped into a single PR
    - Commits use Conventional Commit prefixes (`build(deps)`, `ci(deps)`) and PRs are labelled automatically
    - Spring Boot major version bumps are ignored, since they need manual migration

## Security Considerations

- Tokens are single-use and time-limited
- All endpoints except `/ott/sent` and `/login/ott` require authentication
- The demo user uses `{noop}` (plain-text) password encoding; use a proper `PasswordEncoder` in production
- SMTP credentials are provided through environment variables, never hard-coded
