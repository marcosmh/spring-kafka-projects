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

##
