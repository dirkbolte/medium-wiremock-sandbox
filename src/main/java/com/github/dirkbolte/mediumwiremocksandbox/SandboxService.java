package com.github.dirkbolte.mediumwiremocksandbox;

import com.github.dirkbolte.wiremock.state.StateHelper;
import com.github.dirkbolte.wiremock.state.StateRecordingAction;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Service
public class SandboxService {

    private WireMockServer server;

    @PostConstruct
    void postConstruct() {
        var stateRecordingAction = new StateRecordingAction();
        server = new WireMockServer(options()
            .port(8080)
            .extensions(
                stateRecordingAction,
                new ResponseTemplateTransformer(true, "state", new StateHelper(stateRecordingAction))
            )
            .mappingSource(new JsonFileMappingsSource(new ClasspathFileSource("wiremock"), null)));
        server.start();
    }

    @PreDestroy
    void preDestroy() {
        server.shutdown();
    }

}
