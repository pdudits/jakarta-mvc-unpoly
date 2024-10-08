# Unpoly Demo on Jakarta REST

----
Only At DEVOXX Belgium 2024:
## Spot Prismo and Win a Giant Rubber Duck! 🎉
Here’s how to join the fun:

* Keep your eyes peeled for Prismo hidden in my slides 👀
* Count how many times you see him throughout the presentation
* Submit your guess by tomorrow morning for a chance to win: https://www.payara.fish/page/hide-n-seek-devoxx-be-2024/
* Winner announced Wednesday at 9:15 at Payara Booth
----

## Getting Started

### Prerequisites

- [Java SE 21](https://adoptium.net/?variant=openjdk21)
- [Maven](https://maven.apache.org/download.cgi)

## Running the Application

To run the application locally, follow these steps:

1. Open a terminal and navigate to the project's root directory.

2. Make sure you have the appropriate Java version installed. We have tested with Java SE 8, Java SE 11, Java SE 17, and Java SE 21.

3. Execute the following command:

```
./mvn -Prun
```

4. Once the runtime starts, you can access the project at http://localhost:8080/

## Deploying to Payara Cloud
If you've got your account created at https://billing.payara.cloud (free trial available), follow these steps to deploy the application to Payara Cloud:

Open a terminal and navigate to the project's root directory.
Execute the following Maven command to build the application and deploy to the Payara Cloud:

```
./mvn package payara-cloud:deploy
```