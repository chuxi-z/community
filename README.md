 ## web community
 
 ## material
[spring](https://spring.io/guides)  
[spring](https://spring.io/guides/gs/serving-web-content/)  
[社区](https://www.elasticsearch.cn/explore)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)  
[spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database)  

 ## tools
[git](https://git-scm.com/downloads)  
[visual-paradigm](https://www.visual-paradigm.com/cn/)  
[lombok](https://projectlombok.org)  
[editor.md](https://pandao.github.io/editor.md/)  
 ## script
 Run the migrate and generator commands for database and project construction
```command
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
SQL commands
```sql
CREATE CACHED TABLE USER(
    "ID" INT NOT NULL NULL_TO_DEFAULT SEQUENCE,
    "ACCOUNT_ID" VARCHAR(100),
    "NAME" VARCHAR(50),
    "TOKEN" CHAR(36),
    "GMT_CREATE" BIGINT,
    "GMT_MODIFIED" BIGINT
)
```

 ## operation manual
 ```
1. mvn package
2. java -jar target/community-0.0.1-SNAPSHOT.jar
3. http://localhost:8080
 ```
 ## Notice
 Primary key in Database User and Question may lose auto increment.