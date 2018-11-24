package ru.hse.alice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class JsonErrorController implements ErrorController {
    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String error() {
        return "So bad, it's error!";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

