package mh.poc.module.web1;

import mh.poc.nico.reactive.module.impl.AbstractHttpModule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.reactive.web.dispatch.DispatcherHandler;
import org.springframework.reactive.web.http.HttpHandler;


public class HttpModule extends AbstractHttpModule {

    public HttpModule() {
        super(new AnnotationConfigApplicationContext(ModuleConfig.class));
    }

    @Override
    public HttpHandler rootHandler() {
        return getApplicationContext().getBean(DispatcherHandler.class);
    }
}
