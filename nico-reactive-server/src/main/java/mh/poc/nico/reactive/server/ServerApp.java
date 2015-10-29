package mh.poc.nico.reactive.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.reactive.web.http.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ServerApp {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("jboss.modules.system.pkgs", "org.springframework,mh.poc.nico.reactive,org.reactivestreams,reactor");

        ApplicationContext context = new AnnotationConfigApplicationContext(ServerConfig.class);
        HttpServer server = context.getBean(HttpServer.class);
        server.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            bufferedReader.readLine();
        }
    }
}
