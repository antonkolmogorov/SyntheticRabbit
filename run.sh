chmod +x ./gradlew

./gradlew build

docker build -t requesthandler ./RequestHandler/
docker run -d --rm -p 8080:8080 requesthandler

