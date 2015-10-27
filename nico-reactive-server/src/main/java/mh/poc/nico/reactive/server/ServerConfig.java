package mh.poc.nico.reactive.server;

import com.google.common.io.Files;
import com.netflix.hystrix.Hystrix;
import com.netflix.nicobar.core.archive.JarScriptArchive;
import com.netflix.nicobar.core.execution.HystrixScriptModuleExecutor;
import com.netflix.nicobar.core.module.ScriptModuleLoader;
import com.netflix.nicobar.core.persistence.ArchiveRepository;
import com.netflix.nicobar.core.persistence.ArchiveRepositoryPoller;
import com.netflix.nicobar.core.persistence.PathArchiveRepository;
import com.netflix.nicobar.core.plugin.ScriptCompilerPluginSpec;
import com.netflix.nicobar.core.utils.ClassPathUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.reactive.web.http.HttpHandler;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.reactive.web.http.reactor.ReactorHttpServer;
import org.springframework.reactive.web.http.undertow.UndertowHttpServer;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan("mh.poc.nico.reactive.server")
public class ServerConfig {

//    @Value("${baseArchiveDir:classpath}")
    private String archiveDir="classpath";

//    @Value("${server.port:8080}")
    private int serverPort=8080;

    private File tmpRepoDir;

    public static final String GROOVY2_PLUGIN_ID = "groovy2";
    public static final String GROOVY2_COMPILER_PLUGIN_CLASS = "com.netflix.nicobar.groovy2.plugin.Groovy2CompilerPlugin";

    public static final String BYTECODE_PLUGIN_ID = "bytecode";
    public static final String BYTECODE_COMPILER_PLUGIN_CLASS = "com.netflix.nicobar.core.plugin.BytecodeLoadingPlugin";

    @Bean
    public ScriptModuleLoader scriptModuleLoader(RootHandler rootHandlerListener) throws Exception {
        ScriptModuleLoader moduleLoader = new ScriptModuleLoader.Builder()
                .addPluginSpec(new ScriptCompilerPluginSpec.Builder(GROOVY2_PLUGIN_ID) // configure Groovy plugin
                        .addRuntimeResource(getGroovyRuntime())
                        .addRuntimeResource(getGroovyPluginLocation())
                        .withPluginClassName(GROOVY2_COMPILER_PLUGIN_CLASS)
                        .build())
                .addPluginSpec(new ScriptCompilerPluginSpec.Builder(BYTECODE_PLUGIN_ID)
                        .withPluginClassName(BYTECODE_COMPILER_PLUGIN_CLASS)
                        .build())
                .addListener(rootHandlerListener)
                .build();

        return moduleLoader;
    }

    @Bean
    public ArchiveRepository repository(ClasspathModuleRepository scanner) throws IOException {
        Path baseArchiveDir;

        if ("classpath".equals(archiveDir)){
            tmpRepoDir = Files.createTempDir();
            baseArchiveDir = tmpRepoDir.toPath();
        } else {
            baseArchiveDir = Paths.get(archiveDir);
        }

        ArchiveRepository repository = new PathArchiveRepository.Builder(baseArchiveDir).build();
        if (tmpRepoDir != null) {
            initRepo(scanner, repository);
        }
        return repository;
    }

    @Bean
    public ArchiveRepositoryPoller pooler(ScriptModuleLoader moduleLoader, ArchiveRepository repository) {
        ArchiveRepositoryPoller poller = new ArchiveRepositoryPoller.Builder(moduleLoader).build();
        poller.addRepository(repository, 30, TimeUnit.SECONDS, true);

        return poller;
    }

    @Bean
    public HystrixScriptModuleExecutor<String> executor(){
        return new HystrixScriptModuleExecutor<String>("TestModuleExecutor");
    }


    @Bean
    public HttpServer getServer(HttpHandler rootHandler){
        HttpServer server = new UndertowHttpServer();
//        HttpServer server = new ReactorHttpServer();
        server.setPort(serverPort);
        server.setHandler(rootHandler);
        return server;
    }

    @PreDestroy
    public void shutdown() {
        Hystrix.reset();
        try {
            if (tmpRepoDir != null) {
                FileUtils.deleteDirectory(tmpRepoDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Path getGroovyRuntime() {
        Path path = ClassPathUtils.findRootPathForResource("META-INF/groovy-release-info.properties", getClass().getClassLoader());
        if (path == null) {
            throw new IllegalStateException("couldn't find groovy-all.n.n.n.jar in the classpath.");
        }
        return path;
    }

    Path getGroovyPluginLocation() {
        String resourceName = ClassPathUtils.classNameToResourceName(GROOVY2_COMPILER_PLUGIN_CLASS);
        Path path = ClassPathUtils.findRootPathForResource(resourceName, getClass().getClassLoader());
        if (path == null) {
            throw new IllegalStateException("couldn't find groovy2 plugin jar in the classpath.");
        }
        return path;
    }

    private void initRepo(ClasspathModuleRepository scanner, ArchiveRepository repository) throws IOException {
        scanner.scan().forEach( path -> {
            try {
                JarScriptArchive archive = new JarScriptArchive.Builder(path).build();
                repository.insertArchive(archive);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
