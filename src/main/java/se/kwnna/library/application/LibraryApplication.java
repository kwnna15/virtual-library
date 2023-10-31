package se.kwnna.library.application;

import java.util.function.Predicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@ComponentScan("se.kwnna.library")
@EnableJpaRepositories("se.kwnna.library.datastore")
@EntityScan("se.kwnna.library.datastore.dto")
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.ant("/error")))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(getapiInfo())
                .pathMapping("/");
    }

    private ApiInfo getapiInfo() {
        return new ApiInfoBuilder()
                .title("Bilioteket test")
                .description("API för biblioteket test")
                .build();
    }
}
