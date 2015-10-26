package mh.poc.module.web1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.reactive.codec.encoder.JacksonJsonEncoder;
import org.springframework.reactive.codec.encoder.JsonObjectEncoder;
import org.springframework.reactive.codec.encoder.StringEncoder;
import org.springframework.reactive.web.dispatch.DispatcherHandler;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.reactive.web.dispatch.method.annotation.RequestMappingHandlerMapping;
import org.springframework.reactive.web.dispatch.method.annotation.ResponseBodyResultHandler;

import java.util.Arrays;


@Configuration
@ComponentScan("mh.poc.module.web1")
public class ModuleConfig {

    @Bean
    public RequestMappingHandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }

    @Bean
    public ResponseBodyResultHandler responseBodyResultHandler(){
        return new ResponseBodyResultHandler(Arrays.asList(new StringEncoder(), new JacksonJsonEncoder()), Arrays.asList(new JsonObjectEncoder()));
    }

    @Bean
    public DispatcherHandler dispatcherHandler(){
        return new DispatcherHandler();
    }
}
