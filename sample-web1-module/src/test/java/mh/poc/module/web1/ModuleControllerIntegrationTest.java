package mh.poc.module.web1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.reactive.web.http.HttpServer;
import org.springframework.reactive.web.http.reactor.ReactorHttpServer;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.*;

public class ModuleControllerIntegrationTest {

    private int port;
    private HttpServer server;
    private HttpModule module;

    @Before
    public void setup() throws Exception {
        this.module = new HttpModule();
        this.port = SocketUtils.findAvailableTcpPort();
        this.server = new ReactorHttpServer();
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
    public void testHello(){
        RestTemplate restTemplate = new RestTemplate();

        URI url = URI.create("http://localhost:" + port + "/hello?name=Marek");
        RequestEntity<Void> request = RequestEntity.get(url).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals("Hello Marek!", response.getBody());
    }
}