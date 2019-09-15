# 该镜像需要依赖的基础镜像
FROM java:8
FROM maven:3.5.4-alpine
# 打包
ADD . /blog
WORKDIR /blog/
RUN mvn clean package
RUN ls
EXPOSE 81
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar target/tim-sign-fys-0.0.1-SNAPSHOT.jar
