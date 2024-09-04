package com.korolev.dens.micro;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroController {

    @PostMapping("/sending")
    public void sendMessage() {
    }

}
