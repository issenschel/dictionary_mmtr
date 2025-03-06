package com.example.dictionary_mmtr.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
        "com.example.dictionary_mmtr.initializer",
        "com.example.dictionary_mmtr.utility",
        "com.example.dictionary_mmtr.controller",
        "com.example.dictionary_mmtr.repository",
        "com.example.dictionary_mmtr.service",
        "com.example.dictionary_mmtr.validation"})
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}