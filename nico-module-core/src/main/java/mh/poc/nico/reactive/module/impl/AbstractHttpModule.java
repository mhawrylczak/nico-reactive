package mh.poc.nico.reactive.module.impl;

import mh.poc.nico.reactive.module.HttpModule;
import org.springframework.context.support.AbstractApplicationContext;


public abstract class AbstractHttpModule extends AbstractModule implements HttpModule {
    protected AbstractHttpModule(AbstractApplicationContext applicationContext) {
        super(applicationContext);
    }
}
