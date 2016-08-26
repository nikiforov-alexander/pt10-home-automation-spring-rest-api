# Techdegree project 10
### REST API with Spring: Home Automation
<hr>

### Table of Contents
### Installation instructions
* [Eclipse installation instructions.] (#eclipse)

<hr>

### Misc
- [Quick Links to files and directories] (#links)

<hr>

### Tasks
* [1.] (#task-1)
    Create a Spring Data REST project to serve the HATEOAS compliant API. 
    Use the Spring Boot Gradle plugin for your dependencies.
    <hr>
* [2.] (#task-2)
    Build entities and relationships between Room, Device, Control, and User 
    based on the included diagram.
    <hr>

<!--Links-->

<!--External Links-->

<!--Properties files-->
[initial_project_files]:
    initial-project-files "directory with initial project files from Treeshouse"
[build.gradle]:
    ./build.gradle "Gradle configuration file: build.gradle"
[application.properties]:
    ./src/main/resources/application.properties "Spring Application properties file ./src/main/resources/application.properties"
[rest-messages.properties]:
    ./src/main/resources/rest-messages.properties "File w REST messages, describing our API ./src/main/resources/rest-messages.properties"

<!--Misc-->
<!--
    [model_classes_relationship_diagram.png]:
    ./misc/model_classes_relationship_diagram.png "./misc/model_classes_relationship_diagram.png"
 -->

<!--Java Classes-->
[CustomUserDetailsService]:
    ./src/main/java/com/teamtreehouse/home/service/CustomUserDetailsService.java "./src/main/java/com/teamtreehouse/home/service/CustomUserDetailsService.java"
[Application]:
    ./src/main/java/com/teamtreehouse/home/Application.java "./src/main/java/com/teamtreehouse/home/Application.java"
[BaseEntity]:
    ./src/main/java/com/teamtreehouse/home/model/BaseEntity.java "./src/main/java/com/teamtreehouse/home/model/BaseEntity.java"
[Device]:
    ./src/main/java/com/teamtreehouse/home/model/Device.java "./src/main/java/com/teamtreehouse/home/model/Device.java"
[Control]:
    ./src/main/java/com/teamtreehouse/home/model/Control.java "./src/main/java/com/teamtreehouse/home/model/Control.java"
[User]:
    ./src/main/java/com/teamtreehouse/home/model/User.java "./src/main/java/com/teamtreehouse/home/model/User.java"
[Room]:
    ./src/main/java/com/teamtreehouse/home/model/Room.java "./src/main/java/com/teamtreehouse/home/model/Room.java"
[DataLoader]:
    ./src/main/java/com/teamtreehouse/home/DataLoader.java "./src/main/java/com/teamtreehouse/home/DataLoader.java"
[ControlDao]:
    ./src/main/java/com/teamtreehouse/home/dao/ControlDao.java "./src/main/java/com/teamtreehouse/home/dao/ControlDao.java"
[DeviceDao]:
    ./src/main/java/com/teamtreehouse/home/dao/DeviceDao.java "./src/main/java/com/teamtreehouse/home/dao/DeviceDao.java"
[UserDao]:
    ./src/main/java/com/teamtreehouse/home/dao/UserDao.java "./src/main/java/com/teamtreehouse/home/dao/UserDao.java"
[RoomDao]:
    ./src/main/java/com/teamtreehouse/home/dao/RoomDao.java "./src/main/java/com/teamtreehouse/home/dao/RoomDao.java"
[RestConfig]:
    ./src/main/java/com/teamtreehouse/home/config/RestConfig.java "./src/main/java/com/teamtreehouse/home/config/RestConfig.java"
[WebSecurityConfiguration]:
    ./src/main/java/com/teamtreehouse/home/config/WebSecurityConfiguration.java "./src/main/java/com/teamtreehouse/home/config/WebSecurityConfiguration.java"



### Eclipse Installation instructions
<hr> <a id="eclipse"></a>
Under construction...




### Tasks
1. <a id="task-1"></a>
    Create a Spring Data REST project to serve the HATEOAS compliant API. 
    Use the Spring Boot Gradle plugin for your dependencies.
    <hr>
    Created Spring Data REST project, added following dependencies for
    the project to be HATEOAS compliant:
    - `org.springframework.boot:spring-boot-starter-data-rest`
       : main spring data rest dependecy
    - `org.springframework.boot:spring-boot-starter-data-jpa`:
       Spring Data JPA repositories dependency
    - `org.springframework.data:spring-data-rest-hal-browser`:
      HAL browser dependency

    [Application] runs with @SpringBootApplication annotation to
    ensure proper REST api auto configuration
<hr>
2. <a id="task-2"></a>
    Build entities and relationships between Room, Device, Control, and User 
    based on the included diagram.
    <hr>
    Entities were built according to diagram, see 
    UML diagram 
    ![here](./misc/model_classes_relationship_diagram.png)
<hr>
3. <a id="task-3"></a>
    <hr>
<hr
