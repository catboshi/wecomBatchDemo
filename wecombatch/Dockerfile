FROM java:8
MAINTAINER wecombatch

VOLUME /tmp

COPY wecombatch-0.0.1.RELEASE.jar wecombatch-1.0.0.jar

ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/{TZ} /etc/localtime && echo "{TZ}" > /etc/timezone

EXPOSE 9082

ENTRYPOINT ["java","-jar","wecombatch-1.0.0.jar"]