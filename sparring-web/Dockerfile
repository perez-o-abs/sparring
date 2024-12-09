FROM openjdk:8
RUN wget https://download.ej-technologies.com/jprofiler/jprofiler_linux_7_2_3.tar.gz -P /tmp/ &&\
 tar -xzf /tmp/jprofiler_linux_7_2_3.tar.gz -C /usr/local &&\
 rm /tmp/jprofiler_linux_7_2_3.tar.gz
ENV JPAGENT_PATH="-agentpath:/usr/local/jprofiler9/bin/linux-x64/libjprofilerti.so=nowait"
EXPOSE 8849
ADD target/sparring-web.jar sparring-web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","sparring-web.jar"]