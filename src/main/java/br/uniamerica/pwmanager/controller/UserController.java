package br.uniamerica.pwmanager.controller;

import br.uniamerica.pwmanager.entity.User;
import br.uniamerica.pwmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("me")
    public ResponseEntity<User> getCurrentUser() {
        UsernamePasswordAuthenticationToken currentAuth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok().body(
                (User) currentAuth.getPrincipal()
        );
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Optional<User> registeredUser = this.userService.insert(user);
        if (registeredUser.isPresent()) {
            return ResponseEntity.ok().body(registeredUser);
        }
        return ResponseEntity.badRequest().body("Erro ao registrar");
    }
}
