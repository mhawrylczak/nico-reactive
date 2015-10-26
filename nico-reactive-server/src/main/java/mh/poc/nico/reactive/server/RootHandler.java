package mh.poc.nico.reactive.server;

import com.netflix.nicobar.core.archive.ModuleId;
import com.netflix.nicobar.core.archive.ScriptArchive;
import com.netflix.nicobar.core.module.ArchiveRejectedReason;
import com.netflix.nicobar.core.module.ScriptModule;
import com.netflix.nicobar.core.module.ScriptModuleListener;
import com.netflix.nicobar.core.module.ScriptModuleUtils;
import mh.poc.nico.reactive.module.HttpModule;
import mh.poc.nico.reactive.module.Module;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.reactive.web.http.HttpHandler;
import org.springframework.reactive.web.http.ServerHttpRequest;
import org.springframework.reactive.web.http.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.Publishers;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RootHandler implements ScriptModuleListener, HttpHandler {

    private class ModuleInfo{
        private final ScriptModule scriptModule;
        private final Module appModule;

        private ModuleInfo(ScriptModule scriptModule) throws IllegalArgumentException, InstantiationException, IllegalAccessException {
            this.scriptModule = scriptModule;
            this.appModule = findAppModule(scriptModule);
        }

        private Module findAppModule(ScriptModule scriptModule) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
            Class<Module>  clazz = (Class<Module>) ScriptModuleUtils.findAssignableClass(scriptModule, Module.class);
            if (clazz == null){
                throw new IllegalArgumentException("module "+scriptModule.getModuleId()+" does not contain AppModule");
            }
            return clazz.newInstance();
        }


        public boolean isHttpModule(){
            return appModule instanceof HttpModule;
        }

        public Module getAppModule(){
            return appModule;
        }

        public HttpModule getHttpModule() {
            return (HttpModule) appModule;
        }
    }

    private Map<ModuleId, ModuleInfo> modules = new ConcurrentHashMap<>();

    @Override
    public void moduleUpdated(ScriptModule newScriptModule, ScriptModule oldScriptModule) {
        if (oldScriptModule != null){
            ModuleInfo module = modules.remove(oldScriptModule.getModuleId());
            try {
                module.getAppModule().stop();
                module.getAppModule().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (newScriptModule != null){
            try {
                modules.put(newScriptModule.getModuleId(), new ModuleInfo(newScriptModule));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void archiveRejected(ScriptArchive scriptArchive, ArchiveRejectedReason reason, Throwable cause) {

    }

    @Override
    public Publisher<Void> handle(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServerHttpResponseWrapper wrappedResponse = new ServerHttpResponseWrapper(serverHttpResponse);
        for(ModuleInfo module: modules.values()){
            if (module.isHttpModule()){
                //TODO use executor ?
                Publisher<Void> publisher = module.getHttpModule().rootHandler().handle(serverHttpRequest, wrappedResponse);
                if (!Publishers.empty().equals(publisher)){
                    return publisher;
                }
            }
        }

        serverHttpResponse.setStatusCode(HttpStatus.NOT_FOUND);
        return Publishers.empty();
    }


    private class ServerHttpResponseWrapper implements ServerHttpResponse{
        private final ServerHttpResponse actual;

        private ServerHttpResponseWrapper(ServerHttpResponse actual) {
            this.actual = actual;
        }

        @Override
        public void setStatusCode(HttpStatus httpStatus) {
            if (HttpStatus.NOT_FOUND != httpStatus){
                actual.setStatusCode(httpStatus);
            }
        }

        @Override
        public Publisher<Void> writeWith(Publisher<ByteBuffer> publisher) {
            return actual.writeWith(publisher);
        }

        @Override
        public HttpHeaders getHeaders() {
            return actual.getHeaders();
        }
    }
}
