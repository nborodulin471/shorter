package ru.shorter.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shorter.config.LinkConfig;
import ru.shorter.dao.LinkRepository;
import ru.shorter.model.Link;
import ru.shorter.service.exception.LimitClicksException;
import ru.shorter.service.exception.NoSuchLinkException;

import java.time.Instant;
import java.util.UUID;

/**
 * Сервис по работе со ссылками.
 *
 * @author Бородулин Никита Петрович.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {

    private final UrlShortenerService urlShortenerService;
    private final LinkConfig linkConfig;
    private final LinkRepository linkRepository;

    /**
     * Создает сокращенную ссылку на основании длинной.
     */
    public String create(String longUrl, UUID uuidUser) {
        // проверяем есть ли уже сохраненная ссылка
        var link = linkRepository.findByUserUuidAndLongLink(uuidUser, longUrl)
                .orElse(null);

        // если ссылки нет, тогда создаем новую
        if (link == null) {
            link = new Link();
            link.setLongLink(longUrl);
            link.setUserUuid(uuidUser);
            link.setShortLink(urlShortenerService.shortenUrl(longUrl));
            link.setExpirationDate(Instant.ofEpochMilli(Instant.now().toEpochMilli() + linkConfig.getExpirationDate()));

            linkRepository.save(link);
        }

        return link.getShortLink();
    }

    /**
     * Возвращает длинную ссылку для перехода и инкременитрует кол-во использований.
     */
    public String redirectShortUrl(String shortUrl) {
        var shortLink = String.format("%s%s", linkConfig.getBaseUrl(), shortUrl);

        // ищем сохраненную ссылку в репозитории
        var link = linkRepository.findByShortLink(shortLink)
                .orElseThrow(NoSuchLinkException::new);

        // выкидываем ошибку если количество переходов превышено
        if (link.getClicksLeft() >= linkConfig.getDefaultClicks()) {
            throw new LimitClicksException("Количество переходов превышено");
        }

        // увеличиваем количество срабатываний
        link.setClicksLeft(link.getClicksLeft() + 1);
        linkRepository.save(link);

        log.debug("Для переданной короткой ссылки {} будет осуществлен переход по ссылке {}, осталось переходов {}",
                shortUrl, link.getLongLink(), linkConfig.getDefaultClicks() - link.getClicksLeft());

        return link.getLongLink();
    }
}
