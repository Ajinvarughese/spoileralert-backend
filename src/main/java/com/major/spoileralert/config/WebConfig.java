package com.major.spoileralert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Define file paths using Paths.get() for OS compatibility
        String mediaUploadPath = Paths.get(System.getProperty("user.dir"), "uploads").toUri().toString();
        String categoryImagePath = Paths.get(System.getProperty("user.dir"), "category_images").toUri().toString();

        // Serve uploaded media files
        registry.addResourceHandler("/media/**")
                .addResourceLocations(mediaUploadPath);

        // Serve category images
        registry.addResourceHandler("/category-images/**")
                .addResourceLocations(categoryImagePath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Allow specific origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                .allowedHeaders("*")
                .allowCredentials(true); // Allow cookies/session-based authentication
    }

}
