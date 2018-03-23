#### Version Number
${version-number}

#### New Features
 - [CAF-3493](https://jira.autonomy.com/browse/CAF-3493): CONTENT field passed as metadata reference  
    The family task submitter will now always pass the CONTENT field as a metadata reference, which can be used to verify that metadata references are correctly handled for a family of documents being processed in a workflow.

 - [CAF-3878](https://jira.autonomy.com/browse/CAF-3878): Family task submitter now submits document worker tasks  
    Family task submitter has been changed to submit composite document worker task messages rather than policy worker messages so that it may be used with the new workflow worker implementation for chained workers. RabbitMQ environment variables names used by family submitter have also been changed to be in line with other CAF services. The previous property names are still supported.

 - [SCMOD-3478](https://jira.autonomy.com/browse/SCMOD-3478): Database includes global and tenant config tables  
    The latest version of the core policy database installer is now being used which updates the database container with tables for global and tenant configuration values.
    
 - [SCMOD-3545](https://jira.autonomy.com/browse/SCMOD-3545): Family task submitter now passes tenant ID on submitted tasks 
    The family task submitter will now send a tenant ID in the tasks it submits. The value of this property can be controlled be an environment variable described in the configuration documentation of the family task submitter.

 - [SCMOD-3621](https://jira.autonomy.com/browse/SCMOD-3621): Removed handler registration from database container
    The database container built in this project was registering the open source handler types in the workflow database
    installed as part of its startup. This has been removed. With chained workers the required types are already
    in the database base data.

#### Known Issues
 - None
