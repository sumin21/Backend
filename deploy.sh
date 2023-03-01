#!/bin/bash

REPOSITORY=/root/doyouhave
PROJECT_NAME=Backend

cd $REPOSITORY/$PROJECT_NAME/

echo "> nohup.out 초기화"

cp /dev/null ~/doyouhave/nohup.out

echo "> Git Pull"

git pull

echo "> 프로젝트 Build 시작"

./gradlew build

echo "> REPOSITORY 디렉토리로 이동"

cd $REPOSITORY

echo "> Build 파일 복사"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

echo "현재 구동 중인 애플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR NAME : $JAR_NAME"

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,classpath:/application.properties,/root/doyouhave/env.properties,/root/doyouhave/Backend/src/main/resources/env.properties \
        $REPOSITORY/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
