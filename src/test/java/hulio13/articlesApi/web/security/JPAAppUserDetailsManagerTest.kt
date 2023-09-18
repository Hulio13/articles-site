package hulio13.articlesApi.web.security

import hulio13.articlesApi.infrastructure.data.hibernate.JPAAuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest
@Import(JPAAppUserDetailsManager::class, JPAAppUserDetailsRepository::class)
open class JPAAppUserDetailsManagerTest(@Autowired private val repository: JPAAppUserDetailsRepository,
                                        @Autowired private val manager: JPAAppUserDetailsManager
) {

}