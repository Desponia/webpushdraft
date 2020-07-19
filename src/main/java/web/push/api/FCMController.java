package web.push.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.push.remote.service.FCMService;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService service;

    @GetMapping(value = "/webpush/v1")
    public String sendMessage() throws IOException {
        service.sendMessageTo("", "hello", "body~~");
        return "";
    }
}

