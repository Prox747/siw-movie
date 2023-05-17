package it.uniroma3.siw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SiwMovie00Application {

    public static void main(String[] args) {
        SpringApplication.run(SiwMovie00Application.class, args);
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String absolutePathToImages = "C:/Users/Carlo/IdeaProjects/siw-movie-00/src/main/upload";
            registry.addResourceHandler("/upload/**")
                    .addResourceLocations("file:" + absolutePathToImages + "/")
                    .setCachePeriod(0);
        }
    }

}
