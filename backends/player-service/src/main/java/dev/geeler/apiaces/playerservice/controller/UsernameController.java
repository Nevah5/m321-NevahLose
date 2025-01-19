package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.service.UsernameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usernames")
public class UsernameController {
    @Autowired
    private UsernameService usernameService;

    @GetMapping("/random")
    public String getRandomUsername() {
        return usernameService.getRandomUsername();
    }
}
