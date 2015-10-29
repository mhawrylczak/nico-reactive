package mh.poc.module.http.handler.module1;

import mh.poc.nico.reactive.module.HttpModule;
import mh.poc.nico.reactive.module.impl.AbstractHttpModule;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.reactive.web.http.HttpHandler;


public class HttpHandlerModule implements HttpModule {
    @Override
    public HttpHandler rootHandler() {
        return new HttpSimpleHandler();
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
        return true;
    }
}


