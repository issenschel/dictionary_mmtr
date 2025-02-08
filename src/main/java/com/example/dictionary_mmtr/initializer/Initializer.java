package com.example.dictionary_mmtr.initializer;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.File;

@Component
public class Initializer {

    @Value("${server.port}")
    private Integer port;

    @Value("${webapp.dir}")
    private String webappDirLocation;

    public void init() throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        File webappDir = new File(webappDirLocation);
        tomcat.addWebapp("", webappDir.getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }

}
