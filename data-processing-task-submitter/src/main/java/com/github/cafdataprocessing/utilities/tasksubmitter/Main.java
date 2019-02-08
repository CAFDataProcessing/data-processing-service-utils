/*
 * Copyright 2017-2018 Micro Focus or one of its affiliates.
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
package com.github.cafdataprocessing.utilities.tasksubmitter;

import com.github.cafdataprocessing.processing.service.client.ApiException;
import com.github.cafdataprocessing.utilities.tasksubmitter.environment.ValidateEnvironment;
import com.github.cafdataprocessing.utilities.tasksubmitter.properties.TaskSubmitterProperties;
import com.github.cafdataprocessing.utilities.tasksubmitter.services.Services;
import com.github.cafdataprocessing.utilities.tasksubmitter.taskmessage.TaskMessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main entry point for task submitter application, reading in arguments and performing application function
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        (new Main()).run();
    }

    public void run() throws IOException, InterruptedException, ApiException {
        //check that this is a valid environment to run application in
        ValidateEnvironment.validate();

        TaskSubmitterProperties properties = Services.getInstance().getTaskSubmitterProperties();
        String projectId = properties.getProjectId();

        String documentInputDirectory = properties.getDocumentInputDirectory();
        String workflowName = properties.getWorkflowName();

        if(workflowName==null){
            LOGGER.info("No workflow Name passed. Defaulting to the sample one.");
            workflowName = "sampleworkflow";
        }

        ExecutorService monitorService = TaskMessagePublisher.publishTaskMessagesForDirectoryAndMonitor(documentInputDirectory,
                workflowName, projectId, properties.getSentDocumentsDirectory());
        LOGGER.info("Monitoring directory for new documents.");
        monitorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}
