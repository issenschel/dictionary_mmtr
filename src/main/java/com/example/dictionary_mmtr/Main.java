package com.example.dictionary_mmtr;

import com.example.dictionary_mmtr.config.AppConfig;
import com.example.dictionary_mmtr.initializer.Initializer;
import org.apache.catalina.LifecycleException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;

public class Main {
    public static void main(String[] args) throws ServletException, LifecycleException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(Initializer.class).init();
    }
}










