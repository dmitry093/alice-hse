FROM frolvlad/alpine-oraclejdk8:full

ENV JAVA_OPTS='-XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap'

CMD java \
    ${JAVA_OPTS} \
    -jar /app/alice-hse.jar

COPY build/libs/alice-hse-*.jar /app/alice-hse.jar
