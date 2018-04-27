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

import com.github.cafdataprocessing.processing.service.client.api.TenantConfigurationApi;
import com.github.cafdataprocessing.processing.service.client.api.GlobalConfigurationApi;
import com.github.cafdataprocessing.processing.service.client.model.GlobalConfig;
import com.github.cafdataprocessing.processing.service.client.ApiClient;
import com.github.cafdataprocessing.processing.service.client.ApiException;
import com.github.cafdataprocessing.processing.service.client.model.GlobalConfig.ScopeEnum;

public class ConfigurationsApiClient
{
    private static final String DATA_PROCESSING_SERVICE_URL = System.getenv("DPA_ENDPOINT");
    private static final String DATA_PROCESSING_SERVICE_PORT = System.getenv("PORT");
    private static final String DATA_PROCESSING_SERVICE_GLOBAL_CONFIG_URL
        = DATA_PROCESSING_SERVICE_URL + ":" + DATA_PROCESSING_SERVICE_PORT + "/data-processing-service/v1/globalConfig/";
    private final String dataProcessingServiceCusomConfigsUrl
        = DATA_PROCESSING_SERVICE_URL + ":" + DATA_PROCESSING_SERVICE_PORT + "/data-processing-service/v1/tenants/";
    private final String tenantId;

    public ConfigurationsApiClient(final String tenantId)
    {
        this.tenantId = tenantId;
    }

    public TenantConfigurationApi setTenantConfiguration(final ApiClient apiClient, final String key, final String value)
        throws ApiException
    {
        apiClient.setBasePath(dataProcessingServiceCusomConfigsUrl);
        final TenantConfigurationApi tenantConfigsApiClient = new TenantConfigurationApi(apiClient);
        tenantConfigsApiClient.setTenantConfig(tenantId, key, value);
        return tenantConfigsApiClient;
    }

    public GlobalConfigurationApi setGlobalConfiguration(final ApiClient apiClient, final String key, final String value,
                                                            final String description, final WorkflowConfigScope scope) throws ApiException
    {
        apiClient.setBasePath(DATA_PROCESSING_SERVICE_GLOBAL_CONFIG_URL);
        final GlobalConfig globalConfig = buildGlobalConfig(value, description, ScopeEnum.TENANT);
        final GlobalConfigurationApi globalConfigApiClient = new GlobalConfigurationApi(apiClient);
        globalConfigApiClient.setGlobalConfig(key, globalConfig);
        return globalConfigApiClient;
    }

    private GlobalConfig buildGlobalConfig(final String value, final String descrtiption, final ScopeEnum scope)
    {
        final GlobalConfig config = new GlobalConfig();
        config.setDefault(value);
        config.setDescription(descrtiption);
        config.setScope(scope);
        return config;
    }
}
