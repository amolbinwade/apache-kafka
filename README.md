# Apache Kafka Code Lab : Apache Kafka and Kafka Streams API Design Patterns using SpringBoot 
1. [kafka-spring-producer-consumer](https://github.com/amolbinwade/apache-kafka/tree/main/kafka-spring-producer-consumer)
   #### This module contains code to build Apache Kafka producer and consumer using Spring Framework.
2. [kafka-spring-streams-design-patterns](https://github.com/amolbinwade/apache-kafka/tree/main/kafka-spring-streams-design-patterns)
   #### This module contains Apache kafka Streams API design patterns implmentations.

## Setup Notes:
1. If Kafka is setup using WSL on Windows, then Producer from java application will not directly connect if bootstrap-server is configured as localhost:9092. Follow below steps:

> Identify the ip address of the WSL2 using ifconfig or ip addr or hostname -I

> From the windows command prompt (as admin) run below command

>> netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=172.X.X.X

> 172.X.X.X is the Ip of the WSL2

# 

## Spring Kafka Consumer framework classes
![Alt text](diagrams/spring_kafka_cosumer_framework_diagram.png?raw=true "Spring Framework Classes for Kafka Consumer")

## Kafka Streams API
### Kafka Streams Design Patterns
1. [Single Event Processing](https://github.com/amolbinwade/apache-kafka/blob/main/kafka-spring-streams-design-patterns/src/main/java/com/amcode/kafka/streams/patterns/SingleEventProcessing.java)
