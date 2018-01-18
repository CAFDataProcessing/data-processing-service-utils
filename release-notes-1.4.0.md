#### Version Number
${version-number}

#### New Features
- [CAF-3493](https://jira.autonomy.com/browse/CAF-3493): Family task submitter has been updated to pass the CONTENT field as a metadata reference.
  Family task submitter will now always pass the CONTENT field as a metadata reference, which can be used to verify that metadata references are correctly handled for a family of documents being processed in a workflow.
- [CAF-3878](https://jira.autonomy.com/browse/CAF-3878): Family task submitter now submits document worker tasks.
  Family task submitter has been changed to submit composite document worker task messages rather than policy worker messages so that it may be used with the new workflow worker implementation for chained workers. RabbitMQ environment variables names used by family submitter have also been changed to be in line with other CAF services. The previous property names are still supported.

#### Bug Fixes
