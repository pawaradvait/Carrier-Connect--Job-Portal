package com.CareerConnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final String UPLOAD_DIR = "photos";


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
// Path path = Paths.get(UPLOAD_DIR );
//        registry.addResourceHandler("/"+UPLOAD_DIR+"/**")
//                .addResourceLocations("file:" + path.toAbsolutePath() + "/");
//    }
}
