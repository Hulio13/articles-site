package hulio13.articlesApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class ArticlesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticlesApiApplication.class, args);
    }

}
