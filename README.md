
## 1. APIs
[github登录接入api](https://docs.github.com/cn/developers/apps/building-oauth-apps/creating-an-oauth-app)
[okhttp](https://square.github.io/okhttp/)
[mybatis-generator](http://mybatis.org/generator/index.html)

## maven flyway使用
mvn flyway:init （初始化Flyway metadata ）
mvn flyway:migrate （执行Flyway 升级操作）
mvn flyway:validate （校验Flyway 数据正确性）
mvn clean install flyway:migrate（清理，打包编译project）
## mybatis-generator
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
## swagger-UI 
http://localhost:8080/swagger-ui/index.html
## maven打包
mvn -Dmaven.test.skip=true package

