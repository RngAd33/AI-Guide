# 使用预装 Maven 和 JDK21 的镜像
FROM maven:3.9.9-amazoncorretto-21

# 解决容器时期与真实时间相差 8 小时的问题
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo Asia/Shanghai > /etc/timezone

# 只复制必要的源代码和配置文件
WORKDIR /app
COPY pom.xml .
COPY src ./src

# 使用 Maven 执行打包
RUN mvn package -DskipTests

# 暴露应用端口
EXPOSE 8081

# 使用生产环境配置启动应用
CMD ["java","-jar","/app/target/ai-guide-backend-0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
