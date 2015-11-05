package mh.poc.module.http.handler.echo;

import mh.poc.nico.reactive.module.HttpModule;
import org.springframework.reactive.web.http.HttpHandler;


public class EchoModule implements HttpModule {
    @Override
    public HttpHandler rootHandler() {
        return new EchoHttpHandler();
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
