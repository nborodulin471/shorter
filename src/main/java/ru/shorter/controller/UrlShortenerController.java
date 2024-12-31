package ru.shorter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import ru.shorter.service.LinkService;
import ru.shorter.service.UserUuidService;

/**
 * Контроллер, который отвечает за создание короткой ссылки.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class UrlShortenerController {

    private final LinkService linkService;
    private final UserUuidService userUuidService;

    /**
     * Возвращает основную страницу.
     */
    @GetMapping("/")
    public String showForm() {
        return "urlForm";
    }

    /**
     * Формирует короткую ссылку.
     */
    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String longUrl, Model model, HttpSession session) {
        var uuidUser = userUuidService.generateUuid(session);
        String shortUrl = linkService.create(longUrl, uuidUser);
        model.addAttribute("shortUrl", shortUrl);

        return "result";
    }

}
