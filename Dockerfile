FROM eclipse-temurin:21-jre

WORKDIR /app

# Ambil langsung file .jar dari folder target yang dibuat oleh CodeBuild
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]