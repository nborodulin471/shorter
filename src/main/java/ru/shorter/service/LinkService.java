package ru.shorter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shorter.config.LinkConfig;
import ru.shorter.dao.LinkRepository;
import ru.shorter.model.Link;
import ru.shorter.service.exception.LimitClicksException;
import ru.shorter.service.exception.NoSuchLinkException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.time.Instant.ofEpochMilli;
import static ru.shorter.config.LinkConfig.USER_ATTR;

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
    public void create(String longUrl, UUID uuidUser, long expirationMinutes) {
        // ограничим указанное пользователем время жизни ссылки.
        var ttl = minutesToMilliseconds(linkConfig.getExpirationDateMin());
        var expirationMilliseconds = minutesToMilliseconds(expirationMinutes);
        if (expirationMilliseconds > ttl) {
            ttl = expirationMilliseconds;
        }

        var link = new Link();
        link.setLongLink(longUrl);
        link.setUserUuid(uuidUser);
        link.setShortLink(urlShortenerService.shortenUrl(longUrl));
        link.setExpirationDate(ofEpochMilli(Instant.now().toEpochMilli() + ttl));

        linkRepository.save(link);
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

    /**
     * Получает все сохраненные ссылки сохраненные для пользователя в рамках одной сессии.
     */
    public List<Link> getAllLinksForUser(HttpSession session) {
        var uuidUser = (UUID) session.getAttribute(USER_ATTR);
        return linkRepository.findAllByUserUuid(uuidUser);
    }

    /**
     * Удаляет ссылку из БД.
     */
    @Transactional
    public void deleteLink(UUID uuid) {
        log.debug("Будет удалена запись о ссылке с идентификатором {}", uuid);
        linkRepository.deleteByUuid(uuid);
    }

    /**
     * Обновляет срок действия ссылки.
     */
    public void updateExpiration(UUID uuid, long expirationMinutes) {
        var link = linkRepository.findByUuid(uuid)
                .orElseThrow(NoSuchLinkException::new);

        link.setExpirationDate(ofEpochMilli(link.getExpirationDate().toEpochMilli() + minutesToMilliseconds(expirationMinutes)));

        linkRepository.save(link);
    }

    private long minutesToMilliseconds(long minutes) {
        return minutes * 60 * 1000;
    }
}
