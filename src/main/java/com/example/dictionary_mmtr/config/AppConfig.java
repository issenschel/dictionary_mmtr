package com.example.dictionary_mmtr.config;

import com.example.dictionary_mmtr.enums.DictionaryType;
import com.example.dictionary_mmtr.repository.BaseFileDictionaryRepository;
import com.example.dictionary_mmtr.service.DictionaryFactoryService;
import com.example.dictionary_mmtr.service.DictionaryService;
import com.example.dictionary_mmtr.service.DuplicateDictionaryService;
import com.example.dictionary_mmtr.service.UniqueDictionaryService;
import com.example.dictionary_mmtr.validation.BackspaceValidation;
import com.example.dictionary_mmtr.validation.LatinValidation;
import com.example.dictionary_mmtr.validation.NumberValidation;
import com.example.dictionary_mmtr.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
        "com.example.dictionary_mmtr.initializer",
        "com.example.dictionary_mmtr.utility",
        "com.example.dictionary_mmtr.controller"})
@RequiredArgsConstructor
public class AppConfig {

    private final Environment environment;

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
    public Path latinDictionaryPath() {
        return getDictionaryPath(DictionaryType.LATIN.getDescription());
    }

    @Bean
    public Path numberDictionaryPath() {
        return getDictionaryPath(DictionaryType.NUMBER.getDescription());
    }

    @Bean
    public Path backspaceDictionaryPath() {
        return getDictionaryPath(DictionaryType.BACKSPACE.getDescription());
    }

    private Path getDictionaryPath(String key) {
        String path = environment.getProperty(key);
        return Paths.get(path);
    }

    @Bean
    public Validation latinValidation() {
        return new LatinValidation();
    }

    @Bean
    public Validation numberValidation() {
        return new NumberValidation();
    }

    @Bean
    public Validation backspaceValidation() {
        return new BackspaceValidation();
    }

    @Bean
    public BaseFileDictionaryRepository latinDictionaryRepository(
            Validation latinValidation, Path latinDictionaryPath) {
        return createDictionaryRepository(latinDictionaryPath, latinValidation);
    }

    @Bean
    public BaseFileDictionaryRepository numberDictionaryRepository(
            Validation numberValidation, Path numberDictionaryPath) {
        return createDictionaryRepository(numberDictionaryPath, numberValidation);
    }

    @Bean
    public BaseFileDictionaryRepository backspaceDictionaryRepository(
            Validation backspaceValidation, Path backspaceDictionaryPath) {
        return createDictionaryRepository(backspaceDictionaryPath, backspaceValidation);
    }

    private BaseFileDictionaryRepository createDictionaryRepository(Path path, Validation validation) {
        return new BaseFileDictionaryRepository(path, validation);
    }

    @Bean
    public DictionaryService latinService(BaseFileDictionaryRepository latinDictionaryRepository,
                                          Validation latinValidation,
                                          MessageSource messageSource) {
        return new DuplicateDictionaryService(latinDictionaryRepository, latinValidation, messageSource);
    }

    @Bean
    public DictionaryService numberService(BaseFileDictionaryRepository numberDictionaryRepository,
                                           Validation numberValidation,
                                           MessageSource messageSource) {
        return new DuplicateDictionaryService(numberDictionaryRepository, numberValidation, messageSource);
    }

    @Bean
    public DictionaryService backspaceService(BaseFileDictionaryRepository backspaceDictionaryRepository,
                                              Validation backspaceValidation,
                                              MessageSource messageSource) {
        return new UniqueDictionaryService(backspaceDictionaryRepository, backspaceValidation, messageSource);
    }

    @Bean
    public DictionaryFactoryService dictionaryFactoryService(DictionaryService latinService,
                                                             DictionaryService numberService,
                                                             DictionaryService backspaceService) {
        return new DictionaryFactoryService(latinService, numberService, backspaceService);
    }


}
