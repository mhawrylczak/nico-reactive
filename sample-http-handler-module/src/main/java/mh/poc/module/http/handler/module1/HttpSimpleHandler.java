package mh.poc.module.http.handler.module1;


import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.reactive.web.http.HttpHandler;
import org.springframework.reactive.web.http.ServerHttpRequest;
import org.springframework.reactive.web.http.ServerHttpResponse;
import reactor.Publishers;
import reactor.io.buffer.Buffer;
import reactor.io.codec.json.JsonCodec;
import reactor.rx.Stream;
import reactor.rx.Streams;

public class HttpSimpleHandler implements HttpHandler {
   private  JsonCodec<SamplePojo, SamplePojo> codec = new JsonCodec<>(SamplePojo.class);

    @Override
    public Publisher<Void> handle(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if( !serverHttpRequest.getURI().getPath().equals("/simple") ){
            serverHttpResponse.setStatusCode(HttpStatus.NOT_FOUND);
            return Publishers.empty();
        }


        Stream<SamplePojo> publisher = Streams.just(new SamplePojo("name", 13, "description"));
        Publisher<Buffer> encodedPublisher = codec.encode(publisher);

        serverHttpResponse.setStatusCode(HttpStatus.OK);
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return serverHttpResponse.writeWith(Publishers.map(encodedPublisher, Buffer::byteBuffer) );
    }
}
