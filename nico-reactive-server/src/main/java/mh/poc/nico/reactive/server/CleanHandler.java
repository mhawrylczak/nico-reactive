package mh.poc.nico.reactive.server;

import com.netflix.nicobar.core.module.ScriptModuleLoader;
import com.netflix.nicobar.core.persistence.ArchiveRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.reactive.web.http.HttpHandler;
import org.springframework.reactive.web.http.ServerHttpRequest;
import org.springframework.reactive.web.http.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.Publishers;

import java.io.IOException;

@Component
@Primary
public class CleanHandler implements HttpHandler {

    @Autowired
    private RootHandler rootHandler;

    @Autowired
    private ArchiveRepository repository;

    @Autowired
    private ScriptModuleLoader moduleLoader;


    @Override
    public Publisher<Void> handle(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if( !serverHttpRequest.getURI().getPath().equals("/clean") ){
            return rootHandler.handle(serverHttpRequest, serverHttpResponse);
        }

        try {
            repository.getDefaultView().getArchiveSummaries().stream().forEach(
                    s->{
                        try {
                            moduleLoader.removeScriptModule(s.getModuleId());
                            repository.deleteArchive(s.getModuleId());

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverHttpResponse.setStatusCode(HttpStatus.OK);
        return Publishers.empty();
    }
}
