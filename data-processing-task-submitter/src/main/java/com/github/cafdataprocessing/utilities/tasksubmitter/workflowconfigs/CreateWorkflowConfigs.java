/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cafdataprocessing.utilities.tasksubmitter.workflowconfigs;

import com.github.cafdataprocessing.processing.service.client.ApiClient;
import com.github.cafdataprocessing.processing.service.client.ApiException;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateWorkflowConfigs
{
    private static final String TENANT_ID = System.getenv("TENANTID") != null ? System.getenv("TENANTID") : "DEFAULT";
    private static final String CUSTOM_CONFIGS = "customConfigs.json";

    private CreateWorkflowConfigs()
    {

    }

    public static void createConfigs() throws IOException
    {
        final ApiClient apiClient = new ApiClient();
        final String configs = new String(Files.readAllBytes(Paths.get(CreateWorkflowConfigs.class.getResource(CUSTOM_CONFIGS).getFile())), StandardCharsets.UTF_8);
        final Gson gson = new Gson();
        final WorkflowConfigs workflowConfigs = gson.fromJson(configs, WorkflowConfigs.class);
        final ConfigurationsApiClient configurationsapiClient = new ConfigurationsApiClient(TENANT_ID);
        for (final WorkflowConfig config : workflowConfigs.getConfigs()) {
            try {
                switch (config.getType()) {
                    case GLOBAL:
                        configurationsapiClient.setGlobalConfiguration(apiClient, config.getKey(), config.getValue(),
                                                                       config.getDescription(), WorkflowConfigScope.GLOBAL);
                        break;
                    case CUSTOM:
                        configurationsapiClient.setTenantConfiguration(apiClient, config.getKey(), config.getValue());
                        break;
                    default:
                        break;
                }
            } catch (final ApiException ex) {
                // Eat for now but should log.
            }
        }

    }

}
