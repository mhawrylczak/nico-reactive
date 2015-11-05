package mh.poc.module.web1;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.rx.Streams;

@Controller
public class ModuleController {

    @RequestMapping(value = "/hello", produces = "text/plain")
    @ResponseBody
    public Publisher<String> handle(@RequestParam String name) {
        return Streams.just("Hello ", name, "!");
    }
}
