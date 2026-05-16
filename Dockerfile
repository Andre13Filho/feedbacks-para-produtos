# Estágio 1: Build da aplicação usando Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução no Tomcat 10
FROM tomcat:10.1-jdk17-temurin
WORKDIR /usr/local/tomcat/webapps/

# Remove as aplicações padrão do Tomcat e copia o nosso WAR como ROOT (acessível em /)
RUN rm -rf ROOT && rm -rf examples && rm -rf docs
COPY --from=build /app/target/feedback-app.war ./ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
