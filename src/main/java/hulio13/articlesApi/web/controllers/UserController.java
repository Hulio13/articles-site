package hulio13.articlesApi.web.controllers;

import hulio13.articlesApi.domain.entity.author.AuthorName;
import hulio13.articlesApi.web.entities.Result;
import hulio13.articlesApi.web.security.entities.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@PreAuthorize("permitAll()")
// TODO: Добавить подтверждение с помощью email
public class UserController {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder encoder;

    public UserController(UserDetailsManager userDetailsManager, PasswordEncoder encoder) {
        this.userDetailsManager = userDetailsManager;
        this.encoder = encoder;
    }

    @PostMapping("/register/{username}/{password}/{name}")
    public ResponseEntity<Result<Boolean>> register(@PathVariable String username, @PathVariable String password, @PathVariable String name) {
        AppUserDetails appUserDetails = new AppUserDetails();
        appUserDetails.setUsername(username);
        appUserDetails.setPassword(encoder.encode(password));
        appUserDetails.getAuthor().setName(new AuthorName(name));
        appUserDetails.addRoleIfNotExist("ROLE_AUTHOR");

        userDetailsManager.createUser(appUserDetails);

        return ResponseEntity.ok(Result.ok(true));
    }

    @PutMapping("/changePass/{oldPass}/{newPass}")
    public ResponseEntity<Result<Boolean>> changePass(@PathVariable String oldPass, @PathVariable String newPass) {
        userDetailsManager.changePassword(oldPass, newPass);
        return ResponseEntity.ok(Result.ok(true));
    }
}
