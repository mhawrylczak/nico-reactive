package mh.poc.nico.reactive.module;

import org.springframework.reactive.web.http.HttpHandler;


public interface HttpModule extends Module {
    HttpHandler rootHandler();
}
