# Repository for Apache Kafka coding practice

## Setup Notes:
1. If Kafka is setup using WSL on Windows, then Producer from java application will not directly connect if bootstrap-server is configured as localhost:9092. Follow below steps:

> Identify the ip address of the WSL2 using ifconfig or ip addr or hostname -I

> From the windows command prompt (as admin) run below command

>> netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=172.X.X.X
172.X.X.X is the Ip of the WSL2
