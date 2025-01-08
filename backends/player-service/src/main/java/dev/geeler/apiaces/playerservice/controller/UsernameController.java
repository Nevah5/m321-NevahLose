package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.service.UsernameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") // TODO: restrict this to the frontend URL
@RestController
@RequestMapping("/usernames")
public class UsernameController {
    @Autowired
    private UsernameService usernameService;

    @GetMapping("/random")
    public String getRandomUsername() {
        return usernameService.getRandomUsername();
    }
}
