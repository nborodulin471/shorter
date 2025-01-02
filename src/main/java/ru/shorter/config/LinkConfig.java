package ru.shorter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс конфигурации описывающий работу со ссылками.
 *
 * @author Бородулин Никита Петрович.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "short")
public class LinkConfig {

    public static final String USER_ATTR = "uuidUser";

    private int defaultClicks;
    private int urlLength;
    private String baseUrl;
    private long expirationDateMin;
}
