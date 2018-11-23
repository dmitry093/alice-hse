package ru.hse.alice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SiteController {
    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "<html>" +
                "<title>Hello message</title>" +
                "<head>" +
                "<meta name=\"yandex-verification\" content=\"c811d5851a6236f7\" />" +
                "</head>" +
                "<body>Hello from Alice App!</body>" +
                "</html>";

    }
}
