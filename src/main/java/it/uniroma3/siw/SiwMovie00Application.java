package it.uniroma3.siw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class SiwMovie00Application {

    public static void main(String[] args) {
        SpringApplication.run(SiwMovie00Application.class, args);
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String absolutePathToImages = "C:/Users/Carlo/IdeaProjects/siw-movie/src/main/upload";
            registry.addResourceHandler("/upload/**")
                    .addResourceLocations("file:" + absolutePathToImages + "/")
                    .setCachePeriod(0);
        }
    }

}
