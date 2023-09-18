package hulio13.articlesApi.web.security.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
class Role(
    @Id
    val role: String
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val role1 = o as Role
        return role == role1.role
    }

    override fun hashCode(): Int {
        return role.hashCode()
    }
}
