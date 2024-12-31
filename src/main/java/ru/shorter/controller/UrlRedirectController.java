package ru.shorter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import ru.shorter.service.LinkService;

/**
 * Контроллер, который отвечает за перенаправление на нужный урл, по используемой короткой ссылки.
 *
 * @author Бородулин Никита Петрович.
 */
@Controller
@RequiredArgsConstructor
public class UrlRedirectController {

    private final LinkService linkService;

    /**
     * Осуществляет переход по короткой ссылке.
     */
    @GetMapping("/{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        var redirectUrl = linkService.redirectShortUrl(shortUrl);

        return new RedirectView(redirectUrl);
    }
}
