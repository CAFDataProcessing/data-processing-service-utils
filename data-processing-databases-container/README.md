# data-processing-databases-container

## Summary

A Docker container encapsulating the required databases for a Data Processing environment. The included databases are;

* Workflow database
* Boilerplate database 

This container has been built to operate as a service (by default) but can also be used as a 'utility' container to install the individual databases on an external postgres server instance.

## Environment Variables

The following environment variables can be passed to the container to control its behavior in both service and utility mode.

#### CAF_BOILERPLATE_DB_NAME

The name to use for the installed boilerplate database. Defaults to 'boilerplate'.

#### CAF_POSTGRES_HOST

The postgres host that will be contacted to install databases and base data to. Defaults to the local database inside the container. If installing to an external database server set this to point to the host.

#### CAF_POSTGRES_PORT

The postgres port that will be used for database communication. Defaults to 5432.

#### CAF_WORKFLOW_DB_NAME

The name to use for the installed workflow database. Defaults to 'workflow'.

#### POSTGRES_PASSWORD

The postgres password that will be used for database communication. If installing to an external database this is the password that will be used to contact the external database server.

#### POSTGRES_USER

The postgres user that will be created with permissions for the installed databases. If installing to an external database this is the user that will be used to contact the external database server.

## Service Mode

On startup of the container, the default behavior is for the databases to be installed to a postgres 9.4 instance inside the container. The postgres instance will not be available for connection until the databases setup is completed. This completion can be seen when the container log outputs the line "Completed installation of Data Processing databases.".

## Utility Mode

The scripts used to install each database can be used individually against an external database server instance, rather than an internal instance, by passing the relevant script command to the docker container on startup. On completion of the script the container will exit. Note the tag used for the docker image should match the version of the container that is to be used.

### Install Workflow Database

The script to install the workflow database can be invoked by running the container, passing appropriate environment variables and the location of the install script inside the container, './install_workflow_db.sh'. An example is shown below.

```
docker run --rm \
	-e "CAF_POSTGRES_HOST=mydatabasehost" \
	-e "CAF_WORKFLOW_DB_NAME=install_workflow" \
	-e "POSTGRES_USER=postgres" \
	-e "POSTGRES_PASSWORD=root" \
	cafdataprocessing/data-processing-databases-1.0.0 \
	./install_workflow_db.sh
```

Here the postgres host to install the database to is a machine 'mydatabasehost', passed via CAF_POSTGRES_HOST to the container. Similarly the name of the database, user and password to use are passed.

#### Additional Arguments

The following arguments can also be specified with this command:

fd : Enables the deletion of the existing database for a fresh install, rather than updating the database.
v : Sets logging level to verbose outputting more detail.

```
docker run --rm \
	-e "CAF_POSTGRES_HOST=mydatabasehost" \
	-e "CAF_WORKFLOW_DB_NAME=install_workflow" \
	-e "POSTGRES_USER=postgres" \
	-e "POSTGRES_PASSWORD=root" \
	cafdataprocessing/data-processing-databases-1.0.0 \
	./install_workflow_db.sh -fd -v
```

### Install Boilerplate Database

The script to install the boilerplate database can be invoked by running the container, passing appropriate environment variables and the location of the install script inside the container, './install_boilerplate_db.sh'. An example is shown below.

```
docker run --rm \
  -e "CAF_POSTGRES_HOST=mydatabasehost" \
  -e "CAF_BOILERPLATE_DB_NAME=install_boilerplate" \
  -e "POSTGRES_USER=postgres" \
  -e "POSTGRES_PASSWORD=root" \
  cafdataprocessing/data-processing-databases-1.0.0 \
  ./install_boilerplate_db.sh
```

#### Additional Arguments

The following arguments can also be specified with this command:

fd : Enables the deletion of the existing database for a fresh install, rather than updating the database.
log : Specifies the logging level of the installer. Valid options are: [debug, info, warning, severe, off].

```
docker run --rm \
  -e "CAF_POSTGRES_HOST=mydatabasehost" \
  -e "CAF_BOILERPLATE_DB_NAME=install_boilerplate" \
  -e "POSTGRES_USER=postgres" \
  -e "POSTGRES_PASSWORD=root" \
  cafdataprocessing/data-processing-databases-1.0.0 \
  ./install_boilerplate_db.sh -fd -log=off
```
