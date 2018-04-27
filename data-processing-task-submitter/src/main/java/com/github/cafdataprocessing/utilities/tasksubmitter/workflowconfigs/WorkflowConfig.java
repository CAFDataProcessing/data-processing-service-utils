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

public class WorkflowConfig
{
    private final WorkflowConfigType type;
    private final String key;
    private final String value;
    private final String description;
    
    public WorkflowConfig(final WorkflowConfigType type, final String key, final String value, final String description){
        this.type = type;
        this.key = key;
        this.value = value;
        this.description = description;
    }

    /**
     * @return the type
     */
    public WorkflowConfigType getType()
    {
        return type;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }
    
}
