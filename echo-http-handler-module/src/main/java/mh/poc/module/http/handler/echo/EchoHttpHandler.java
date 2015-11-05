package mh.poc.module.http.handler.echo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpStatus;
import org.springframework.reactive.web.http.HttpHandler;
import org.springframework.reactive.web.http.ServerHttpRequest;
import org.springframework.reactive.web.http.ServerHttpResponse;
import reactor.Publishers;
import reactor.rx.Streams;

import java.nio.ByteBuffer;

public class EchoHttpHandler implements HttpHandler {

    @Override
    public Publisher<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
        if( !request.getURI().getPath().equals("/echo") ){
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return Publishers.empty();
        }

        request.getBody().subscribe(new Subscriber<ByteBuffer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(1000);
            }

            @Override
            public void onNext(ByteBuffer byteBuffer) {
                System.out.println("onNext:");
                System.out.println(byteBuffer.toString());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response.writeWith(Streams.just(ByteBuffer.wrap("s".getBytes())));
    }
}