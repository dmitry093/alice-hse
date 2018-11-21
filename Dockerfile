FROM gradle:4.8.1-jdk8-alpine as compiler
USER root

WORKDIR /code
ENV GRADLE_USER_HOME=/Dependencies
ADD ./settings.gradle /code
ADD ./build.gradle /code
ADD ./src /code/src

RUN gradle build
RUN mkdir -p /app && \
    cp build/libs/alice-hse-*.jar /app/alice-hse.jar

FROM frolvlad/alpine-oraclejdk8:full

ENV JAVA_OPTS='-XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap'

CMD java \
    ${JAVA_OPTS} \
    -jar /app/alice-hse.jar

COPY --from=compiler /app /app