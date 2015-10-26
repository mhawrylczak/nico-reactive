package mh.poc.nico.reactive.server;

import com.netflix.nicobar.core.archive.ModuleId;
import com.netflix.nicobar.core.execution.HystrixScriptModuleExecutor;
import com.netflix.nicobar.core.execution.ScriptModuleExecutable;
import com.netflix.nicobar.core.module.ScriptModule;
import com.netflix.nicobar.core.module.ScriptModuleLoader;
import com.netflix.nicobar.core.module.ScriptModuleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


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
