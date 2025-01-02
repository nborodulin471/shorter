package ru.shorter.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Класс для отображения объекта {@link Link}.
 *
 * @author Бородулин Никита Петрович.
 */
@Data
@Builder
public class LinkDto {

    private UUID uuid;
    private String longUrl;
    private String shortUrl;
    private String expirationDate;


    /**
     * Преобразует объект, для отражения на клиенте.
     */
    public static LinkDto map(Link entity) {
        var europeanDatePattern = "dd.MM.yyyy:HH:mm:ss";
        var localDate = LocalDateTime.ofInstant(entity.getExpirationDate(), ZoneOffset.UTC);
        var formatLocalDate = localDate.format(DateTimeFormatter.ofPattern(europeanDatePattern));

        return LinkDto.builder()
                .uuid(entity.getUuid())
                .longUrl(entity.getLongLink())
                .shortUrl(entity.getShortLink())
                .expirationDate(formatLocalDate)
                .build();
    }
}
