package com.neom.neims.docmanagement.config;

import com.neom.neims.docmanagement.enumconverters.StringToApprovalStatusEnumConverter;
import com.neom.neims.docmanagement.enumconverters.StringToCategoryEnumConverter;
import com.neom.neims.docmanagement.enumconverters.StringToLifecycleEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToCategoryEnumConverter());
        registry.addConverter(new StringToLifecycleEnumConverter());
        registry.addConverter(new StringToApprovalStatusEnumConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
