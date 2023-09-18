package hulio13.articlesApi.web.controllers

import hulio13.articlesApi.domain.entity.author.AuthorName
import hulio13.articlesApi.domain.entity.common.Result
import hulio13.articlesApi.web.security.entities.AppUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()") // TODO: Добавить подтверждение с помощью email
//TODO: вынести логику создания пользователя в сервис
class UserController(private val userDetailsManager: UserDetailsManager, private val encoder: PasswordEncoder) {
    @PostMapping("/register/{username}/{password}/{name}")
    fun register(
        @PathVariable username: String,
        @PathVariable password: String,
        @PathVariable name: String
    ): ResponseEntity<Result<Boolean>> {
        val appUserDetails = AppUserDetails(username, encoder.encode(password))
        appUserDetails.author.name = AuthorName(name)
        appUserDetails.addRoleIfNotExists("ROLE_AUTHOR")
        userDetailsManager.createUser(appUserDetails)
        return ResponseEntity.ok(Result.ok(true))
    }

    @PutMapping("/changePass/{oldPass}/{newPass}")
    fun changePass(
        @PathVariable oldPass: String?,
        @PathVariable newPass: String?
    ): ResponseEntity<Result<Boolean>> {
        userDetailsManager.changePassword(oldPass, newPass)
        return ResponseEntity.ok(Result.ok(true))
    }
}
