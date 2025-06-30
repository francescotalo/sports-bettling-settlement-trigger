# Sports Betting Settlement Trigger Service

This repository contains the 2 microservices necessary to implement a sports betting trigger service. Ideally in a normal setting we should have 2 different github repositories, one for each of the microservices. However considering the solution has to be provided in one single github repository, I have created this repo and placed each service under a different folder.


## Prerequisites

In order to run the services locally your system has to have the following:

 - Apache Kafka running locally on port 9092. You might want to follow instructions [here](https://developer.confluent.io/confluent-tutorials/kafka-on-docker/) on how to do that
 - Java version 17 or later installed

## How to run the services

The system is composed by two different microservices:

**Sporty Rest Api**: It exposes the rest endpoint necessary to post an event outcome and store in the relevant Kafka Topic event-outcomes. The service code is contained under the folder sporty_rest_api. The service is configured to run locally on port 9090. If you want to change the port number go to sporty_rest_api/src/main/resources/application.properties and change accordingly the value of the property *server.port*. To run the service do the following once you are located in the root directory of the repository:

    cd ./sporty_rest_api
    ./mvnw clean spring-boot:run

 **Sporty Events Consumer**: It contains the logic of the consumer of the Kafka topic event-outcomes. The logic will find in the DB the eventual bets to settle connected with the eventId contained in the event and trigger the settlement process sending data to RocketMQ. The interaction with RocketMQ is mocked in the context of this exercise. The service code is contained under the folder sporty_events_consumer. To run the service do the following once you are located in the root directory of the repository:

    cd ./sporty_events_consumer
    ./mvnw clean spring-boot:run

## How to test the services

First of all it is necessary to send a POST request to the API endpoint like:

    curl --location 'http://localhost:9090/eventOutcome' \ 
    --header 'Content-Type: application/json' \ 
    --data '{ 
    "eventId":2349,
    "eventWinnerId":4488,
    "eventName":"name"
    }'


The eventId and eventName are mandatory fields. If the eventWinnerId is not populate the bet has been lost , otherwise the bet has been won.
Once this has been performed successfully in the output console of the API service you will see a log like:

    API: Received eventOutcome to store : EventOutcome [eventId=2349, eventWinnerId=4488, eventName=name] 
    API: EventOutcome processed successfully: EventOutcome [eventId=2349, eventWinnerId=4488, eventName=name]
  
Now it will be responsability of the events consumer microservice to consume the message in the event-outcomes topic.

The data contained into the bet database are pre-populated performing the statements inside the script sporty_events_consumer/src/main/resources/data.sql when the events consumer service is started. If you want to change the data pre-population modify accordingly that file before starting the events consumer service.

Once the message is picked up from the topic, the consumer will look in  the DB for relevant bets to be settled connected to the event received with the query

    Select * 
    from Bets 
    where event_id = :EVENT_ID and event_winner_id IS NOT NULL
    
where :EVENT_ID is the value contained in the eventId field of the message.

Be aware that the additional condition `and event_winner_id IS NOT NULL` is added as an optimization step because if the event_winner_id is populated it means that the betting settlement has already been triggered for that record and therefore it can be skipped.

For each of the element returned by the query two steps as performed in this sequence:

 1. Send bet record to RocketMQ topic bet-settlements
 2. Update the event_winner_id in the DB record with the corresponding element in the message
 
So with the example mentioned above and the current DB configuration you will see in the console output of the events consumer application something like :


    event outcome message to process: EventOutcome [eventId=2349,eventWinnerId=4488, eventName=name]
    Bet to settle sent to RocketMQ : Bet [id=3, userId=33, eventId=2349, eventMarketId=22, eventWinnerId=4488, amount=326.5] 
    Bet to settle sent to RocketMQ : Bet [id=4, userId=33, eventId=2349, eventMarketId=22, eventWinnerId=4488, amount=426.5] 
    event outcome message consumed successfully: EventOutcome [eventId=2349, eventWinnerId=4488, eventName=name] processed.  
    
  
Be aware that the bet with id=2 has not been picked up despite belongs to the same event_id because the field event_winner_id field is already populated.

If you re submit with the POST API the same event outcome data, you will see that the consumer will not pick up any bet at all because all the bets connected to that event_id have already the event_winner_id field populated. Therefore the console output will show something like

    event outcome message to process: EventOutcome [eventId=2349,eventWinnerId=4488, eventName=name] 
    event outcome message consumed successfully: EventOutcome [eventId=2349, eventWinnerId=4488, eventName=name] processed.
  
