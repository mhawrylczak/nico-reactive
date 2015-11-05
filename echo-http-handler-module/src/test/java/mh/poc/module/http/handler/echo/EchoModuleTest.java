package mh.poc.module.http.handler.echo;

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
import java.util.Random;

import static org.junit.Assert.*;

public class EchoModuleTest {

    private static final int REQUEST_SIZE = 4096 * 8;

    private int port;
    private HttpServer server;
    private EchoModule module;

    private Random rnd = new Random();

    @Before
    public void setup() throws Exception {
        this.module = new EchoModule();
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
    public void echoString() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String body = randomString();
        RequestEntity<String> request = RequestEntity.post(new URI("http://localhost:" + port+"/echo")).body(body);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        assertEquals(body, response.getBody());
    }

    private String randomString() {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        while (builder.length() < REQUEST_SIZE) {
            builder.append(randomChar());
            if (i % 5 == 0) {
                builder.append(' ');
            }
            if (i % 80 == 0) {
                builder.append('\n');
            }
            i++;
        }
        return builder.toString();
    }

    private char randomChar() {
        return (char) (rnd.nextInt(26) + 'a');
    }
}