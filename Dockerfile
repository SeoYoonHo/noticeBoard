# base-image
FROM openjdk:17-alpine
# 변수 설정(빌드파일의 경로)
ARG JAR_FILE=build/libs/*.jar
# 빌드파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar
# jar 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]