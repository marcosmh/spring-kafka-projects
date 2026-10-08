# spring-kafka-projects
Projects with Spring Boot and Kafka

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
sudo docker exec -it kafka /opt/kafka/bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create --topic products.replies \
--partitions 1 --replication-factor 1

## View all Kafka consumer messages in real time products.replies
sudo docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic products.replies \
--from-beginning
