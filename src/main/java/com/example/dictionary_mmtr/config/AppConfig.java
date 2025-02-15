package com.example.dictionary_mmtr.config;

import com.example.dictionary_mmtr.entity.Backspace;
import com.example.dictionary_mmtr.entity.Latin;
import com.example.dictionary_mmtr.entity.Number;
import com.example.dictionary_mmtr.repository.BackspaceRepository;
import com.example.dictionary_mmtr.repository.LatinRepository;
import com.example.dictionary_mmtr.repository.NumberRepository;
import com.example.dictionary_mmtr.service.DictionaryFactoryService;
import com.example.dictionary_mmtr.service.DictionaryService;
import com.example.dictionary_mmtr.service.DuplicateDictionaryService;
import com.example.dictionary_mmtr.service.UniqueDictionaryService;
import com.example.dictionary_mmtr.validation.BackspaceValidation;
import com.example.dictionary_mmtr.validation.LatinValidation;
import com.example.dictionary_mmtr.validation.NumberValidation;
import com.example.dictionary_mmtr.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
        "com.example.dictionary_mmtr.initializer",
        "com.example.dictionary_mmtr.utility",
        "com.example.dictionary_mmtr.controller",
        "com.example.dictionary_mmtr.repository",})
@EnableJpaRepositories(basePackages = "com.example.dictionary_mmtr.repository")
@RequiredArgsConstructor
public class AppConfig {

    @Autowired
    private LatinRepository latinDictionaryRepository;
    @Autowired
    private NumberRepository numberDictionaryRepository;
    @Autowired
    private BackspaceRepository backspaceDictionaryRepository;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.dictionary_mmtr.entity");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/dictionary_mmtr");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }


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
    public DictionaryService latinService(Validation latinValidation, MessageSource messageSource) {
        return new DuplicateDictionaryService<>(latinDictionaryRepository, latinValidation, messageSource, Latin.class);
    }

    @Bean
    public DictionaryService numberService(Validation latinValidation, MessageSource messageSource) {
        return new DuplicateDictionaryService<>(numberDictionaryRepository, latinValidation, messageSource, Number.class);
    }

    @Bean
    public DictionaryService backspaceService(Validation backspaceValidation, MessageSource messageSource) {
        return new UniqueDictionaryService<>(backspaceDictionaryRepository, backspaceValidation, messageSource, Backspace.class);
    }

    @Bean
    public DictionaryFactoryService dictionaryFactoryService(DictionaryService latinService,
                                                             DictionaryService numberService,
                                                             DictionaryService backspaceService) {
        return new DictionaryFactoryService(latinService, numberService, backspaceService);
    }


}
