/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
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
package com.github.cafdataprocessing.utilities.tasksubmitter.initialize.boilerplate;

import com.hpe.caf.util.boilerplate.creation.BoilerplateCreator;

/**
 * Invokes the Boilerplate Creation utility, setting up the environment it requires to run.
 */
public class BoilerplateInvoker {
    private BoilerplateCreator boilerplateCreator;

    /**
     * Create a new configured instance of Boilerplate Invoker.
     * @param inputFile Location of boilerplate expressions/tags file.
     * @param outputFile File location to output details of created boilerplate expressions/tags.
     * @param boilerplateApiUrl URL of the boilerplate API to contact to create expressions/tags.
     */
    public BoilerplateInvoker(String inputFile, String outputFile, String boilerplateApiUrl){
        //set up the properties that creation tool relies on
        System.setProperty("file.input", inputFile);
        System.setProperty("file.output", outputFile);
        System.setProperty("boilerplateapi.url", boilerplateApiUrl);

        boilerplateCreator = new BoilerplateCreator();
    }

    /**
     * Run the configured boilerplate creation.
     */
    public void run(){
        boilerplateCreator.run();
    }
}
