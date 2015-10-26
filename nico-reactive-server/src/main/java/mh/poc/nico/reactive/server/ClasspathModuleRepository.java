package mh.poc.nico.reactive.server;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class ClasspathModuleRepository {

    public Stream<Path> scan() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:/moduleSpec.json");
        return Arrays.stream(resources).map(r -> {
            try {
                return r.getURL();
            } catch (IOException e){
                return null;
            }}).filter(url->url != null && url.getProtocol() == "jar")
                .map( url -> {
                    String path = url.getPath();
                    int idx = path.lastIndexOf('!');
                    return Paths.get(URI.create(path.substring(0, idx)));
                } );
        }
    }
