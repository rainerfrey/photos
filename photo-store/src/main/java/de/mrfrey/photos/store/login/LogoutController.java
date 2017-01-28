package de.mrfrey.photos.store.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/logout")
public class LogoutController {
    @RequestMapping("/success")
    public Map logoutSuccess() {
        return Collections.singletonMap("success", true);
    }
}