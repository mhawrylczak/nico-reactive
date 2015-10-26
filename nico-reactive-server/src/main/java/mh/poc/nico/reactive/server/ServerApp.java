package mh.poc.nico.reactive.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.reactive.web.http.HttpServer;


@SpringBootApplication
public class ServerApp implements CommandLineRunner {

    @Autowired
    private HttpServer server;

    public static void main(String[] args) {
        System.setProperty("jboss.modules.system.pkgs", "org.springframework,mh.poc.nico.reactive");
        SpringApplication.run(ServerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }


}
