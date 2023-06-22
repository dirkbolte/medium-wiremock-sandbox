/*
 * Copyright (C) 2023 Dirk Bolte
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
