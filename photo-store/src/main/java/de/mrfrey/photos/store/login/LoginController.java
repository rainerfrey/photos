package de.mrfrey.photos.store.login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public Map loginPage() {
        return Collections.emptyMap();
    }

    @RequestMapping(value = "/success")
    public Principal success(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/denied", method = GET)
    @ResponseStatus(UNAUTHORIZED)
    public Map denied() {
        return singletonMap("error", singletonMap("code", "springSecurity.errors.login.fail"));
    }
}