package mh.poc.module.http.handler.module1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.reactive.web.http.reactor.ReactorHttpServer;
import org.springframework.reactive.web.http.undertow.UndertowHttpServer;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.*;

public class HttpSimpleHandlerIntegrationTest {

    private int port;
    private HttpServer server;
    private HttpHandlerModule module;

    @Before
    public void setup() throws Exception {
        this.module = new HttpHandlerModule();
        this.port = SocketUtils.findAvailableTcpPort();
        this.server = new UndertowHttpServer();
        this.server.setPort(this.port);
        this.server.setHandler(module.rootHandler());
        this.server.afterPropertiesSet();
        this.server.start();
    }

    @After
    public void tearDown() throws Exception {
        this.server.stop();
    }

    @Test
    public void testHandler(){
        RestTemplate restTemplate = new RestTemplate();

        URI url = URI.create("http://localhost:" + port + "/simple");
        RequestEntity<Void> request = RequestEntity.get(url).build();
        ResponseEntity<SamplePojo> response = restTemplate.exchange(request, SamplePojo.class);

        assertEquals("name", response.getBody().getName());
        assertEquals(13, response.getBody().getAnInt());
        assertEquals("description", response.getBody().getDescription());
    }

}