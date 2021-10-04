chmod +x ./gradlew

./gradlew clean bootJar

docker-compose build .

