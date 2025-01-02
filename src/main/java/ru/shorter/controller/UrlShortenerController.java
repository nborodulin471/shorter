package ru.shorter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import ru.shorter.model.Link;
import ru.shorter.model.LinkDto;
import ru.shorter.service.LinkService;
import ru.shorter.service.UserUuidService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер, который отвечает за создание короткой ссылки.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class UrlShortenerController {

    public static final String ATTRIBUTE_NAME_LINKS = "links";
    public static final String URL_FORM = "urlForm";
    public static final String REDIRECT_TO_ROOT = "redirect:/";

    private final LinkService linkService;
    private final UserUuidService userUuidService;

    /**
     * Возвращает основную страницу.
     */
    @GetMapping("/")
    public String showForm(Model model, HttpSession session) {
        List<Link> links = linkService.getAllLinksForUser(session);

        var linkDtos = links.stream()
                .map(LinkDto::map)
                .toList();

        model.addAttribute(ATTRIBUTE_NAME_LINKS, linkDtos);

        return URL_FORM;
    }

    /**
     * Формирует короткую ссылку.
     */
    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String longUrl, @RequestParam long expirationMinutes, HttpSession session) {
        var uuidUser = userUuidService.generateUuid(session);
        linkService.create(longUrl, uuidUser, expirationMinutes);
        return REDIRECT_TO_ROOT;
    }

    /**
     * Обновляет срок действия ссылки.
     */
    @PostMapping("/updateExpiration")
    public String updateExpiration(@RequestParam UUID uuid, @RequestParam long expirationMinutes) {
        linkService.updateExpiration(uuid, expirationMinutes);
        return REDIRECT_TO_ROOT;
    }

    /**
     * Удаляет ссылку.
     */
    @PostMapping("/deleteLink")
    public String deleteLink(@RequestParam UUID uuid) {
        linkService.deleteLink(uuid);
        return REDIRECT_TO_ROOT;
    }

}
