chmod +x ./gradlew

./gradlew clean bootJar

docker build -t requesthandler ./RequestHandler/

docker-compose
#docker run --rm -p 8080:8080 requesthandler

