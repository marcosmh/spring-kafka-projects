# spring-kafka-projects
Projects with Spring Boot and Kafka

## Development Environment
- OS: Debian GNU/Linux 13 (trixie) x86_64
- Host: Windows Subsystem for Linux - Debian (2.7.14.0)
- Kernel: Linux 6.18.33.2-microsoft-standard-WSL2
- CPU: AMD Ryzen 7 250 (16) @ 3.29 GHz
- Memory: 2.41 GiB / 7.41 GiB (33%)

## Install Docker on WSL Debian 13
* sudo apt update
* sudo apt upgrade -y
* sudo apt install ca-certificates curl
* sudo install -m 0755 -d /etc/apt/keyrings
* sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
* sudo chmod a+r /etc/apt/keyrings/docker.asc
* sudo tee /etc/apt/sources.list.d/docker.sources <<EOF\
Types: deb\
URIs: https://download.docker.com/linux/debian\
Suites: $(. /etc/os-release && echo "$VERSION_CODENAME")\
Components: stable\
Architectures: $(dpkg --print-architecture)\
Signed-By: /etc/apt/keyrings/docker.asc\
EOF

* sudo apt update
* sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
* sudo systemctl status docker
* sudo systemctl start docker
* sudo docker run hello-world
* docker login

## Commands Docker
* sudo systemctl status docker
* sudo systemctl start docker
* docker ps
* docker container ls
* docker container ls -a
* docker images
* docker start kafka
* docker start mysql
* docker logs -f mysql
* docker logs -f kafka
* docker rmi ´images´

## Create a Kafka container
* sudo docker run -d --name kafka -p 9092:9092 apache/kafka:4.2.0

## Create topic products.commands
sudo docker exec -it kafka /opt/kafka/bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create --topic products.commands \
--partitions 1 --replication-factor 1

## List Kafka topics
* sudo docker exec -it kafka /opt/kafka/bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--list

## View all Kafka consumer messages in real time
* sudo docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic products.commands \
--from-beginning

## Test Kafka
curl -X POST http://localhost:8080/products \
-H "Content-Type: application/json" \
-d '{"name": "Teclado Mecanico","price":12.9}'

## Install MySQL on WSL Debian 13
* sudo docker run -d --name mysql -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=enter_password \
-e MYSQL_DATABASE=db_spring_kafka mysql:latest

## Config MySQL
* sudo docker exec -it mysql mysql -uroot -pyour_password -e "SHOW DATABASES;"
* sudo docker exec -it mysql mysql -uroot -pyour_password;
* use db_spring_kafka;
* CREATE TABLE inventory(id INT AUTO_INCREMENT PRIMARY KEY, quantity INT DEFAULT 0);
* show tables;
* insert into inventory(quantity) values(10);
* alter table inventory add COLUMN sku varchar(45) NULL AFTER quantity;

## Create topic products.replies
* sudo docker exec -it kafka /opt/kafka/bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create --topic products.replies \
--partitions 1 --replication-factor 1

## View all Kafka consumer messages in real time products.replies
* sudo docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic products.replies \
--from-beginning

## Dockerizing Spring Cloud microservices
1) Pull the image
* docker pull provectuslabs/kafka-ui:latest

2) Remove the current Kafka container
* docker ps
* docker rm -f kafka

3) Create a Docker network

* docker network create kafka-net
* docker network ls

4) Restart Kafka, advertising it as `kafka:9092` on the network, as it requires
minimal KRaft configuration (roles, controller quorum, listeners, etc.)

Generate a Cluster ID (once):

MacOS and Linux:

CLUSTER_ID=$(docker run --rm apache/kafka:latest /opt/kafka/bin/kafka-storage.sh random-uuid)
echo $CLUSTER_ID

k-7N1XvMRRGwPA4jgM1CnQ

Alternatively, generate the ID and copy the UUID manually:
docker run --rm apache/kafka:latest /opt/kafka/bin/kafka-storage.sh random-uuid

or use this one: q1Sh-9YxT5u8cKzR2mVnLg

## Then run the command to create Kafka:

* sudo docker run -d \
--name kafka \
--network kafka-net \
-p 9092:9092 \
-e KAFKA_NODE_ID=1 \
-e KAFKA_CLUSTER_ID=$CLUSTER_ID \
-e KAFKA_PROCESS_ROLES=broker,controller \
-e KAFKA_CONTROLLER_QUORUM_VOTERS=1@kafka:9093 \
-e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,INTERNAL://0.0.0.0:19092,CONTROLLER://0.0.0.0:9093 \ 
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092,INTERNAL://kafka:19092 \ 
-e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,INTERNAL:PLAINTEXT,CONTROLLER:PLAINTEXT \ 
-e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \ 
-e KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL \ 
-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \ 
-e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \ 
-e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \ 
-e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \ 
apache/kafka:latest

5) Raise Kafka UI pointing to the kafka container name:

* sudo docker run -d \ 
--name kafka-ui \ 
--network kafka-net \ 
-p 8085:8080 \ 
-e KAFKA_CLUSTERS_0_NAME=local \ 
-e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:19092 \ 
provectuslabs/kafka-ui:latest

## Kafka UI
6) Open in browser, go to: 
* http://localhost:8085


## create images
* products-api
./mvnw clean -DskipTests package
sudo docker build -t products-api:latest .

* products-command
./mvnw clean -DskipTests package
sudo docker build -t products-command:latest .

## create network 
* sudo docker network connect kafka-net mysql

## Up microservices
* sudo docker run -d --name products-command --network kafka-net \
-p 8081:8081 products-command:latest

* sudo docker run -d --name products-api --network kafka-net \
-p 8080:8080 products-api:latest