package mh.poc.nico.reactive.module.impl;

import mh.poc.nico.reactive.module.Module;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractModule implements Module{

    private final AbstractApplicationContext applicationContext;

    protected AbstractModule(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void start() {
        applicationContext.start();
    }

    @Override
    public void stop() {
        applicationContext.stop();
    }

    @Override
    public void close() throws Exception {
        applicationContext.close();
    }

    @Override
    public boolean isRunning() {
        return applicationContext.isRunning();
    }

    protected AbstractApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
