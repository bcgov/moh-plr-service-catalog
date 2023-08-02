 
The Service Catalogue is a service that will expose the BC Catalogue HEalthcaerService resources (definied in the PLR FHIR Implementation Guide).
A BC Catalogue HealthcareService profile is the definition of a HEalthcareService, without any link to an organization, Practitionner, Facility, Clinic or Hospital.
The intent of this Service Catalog is to provide a way to see the list of services that are provided in the Province and to search trhough them.
Potential use cases are:
- creating a Referral for a specific service
- searching for a clinic/practionner that is providing a service

The service Catlogue provide 2 REST services:
- GET by Id
- Search by name, system, specialty, Type, parent or external Identifier.

The Service Catlogue is a Spring Boot application relying on a PostgreSQL Database. This is highly portable and easy to deploy and use.
The Database is independmet of the PLR database.

To install it, follow the instructions:
- install postgreSQL (version higher than 13)
- run the DB script, you will need to provide a password for the proxy user in the script database\dcr-001\01_create_schema_service_catalogue
- add the password you choose in config/application.properties
- run the following mvn spring boot command at the root of the project, it will build and run the application. it might take some times the first time as the dependencies needs to be downloaded.
