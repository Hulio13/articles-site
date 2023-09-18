package hulio13.articlesApi.web.dto

import javax.swing.text.StyledEditorKit.BoldAction

data class UserDto(val username: String? = null,
                   val email: String? = null,
                   val password: String? = null,
                   val locked: Boolean? = null,
                   val expired: Boolean? = null,
                   val emailConfirmed: Boolean? = null,
                   val roles: List<String> = emptyList())
