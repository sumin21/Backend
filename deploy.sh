#!/bin/bash

REPOSITORY=/root/doyouhave
PROJECT_NAME=Backend

cd $REPOSITORY/$PROJECT_NAME/

echo "> nohup.out ???"

cp /dev/null ~/doyouhave/nohup.out

echo "> Git Pull"

git pull

echo "> ???? Build ??"

./gradlew build

echo "> REPOSITORY ????? ??"

cd $REPOSITORY

echo "> Build ?? ??"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "?? ???? ?????? pid ??"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "?? ?? ?? ?????? pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> ?? ?? ?? ??????? ???? ???? ????."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> ? ?????? ??"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR NAME : $JAR_NAME"

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,classpath:/application.properties,/root/doyouhave/env.properties,/root/doyouhave/Backend/src/main/resources/env.properties \
        $REPOSITORY/$JAR_NAME 2>&1 &
