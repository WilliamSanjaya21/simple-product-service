FROM eclipse-temurin:21-jre

WORKDIR /app

# Menggunakan pencarian bintang ganda untuk memastikan file .jar di dalam folder target mana pun terbawa
COPY **/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]