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
* [3.] (#task-3)
    Build repositories that can be paged and sorted 
    for each of the entities.
    <hr>
* [4.] (#task-4)
    Expose the API under /api/v1/
    <hr>
* [5.] (#task-5)
    Secure the API so that only authenticated users can view details
    <hr>
* [6.] (#task-6)
    Ensure that the users repository is not exported
    <hr>
* [7.] (#task-7)
    Secure the API so that only users with the ROLE_ADMIN role 
    can create rooms
    <hr>
* [8.] (#task-8)
    Validate that room’s area is less than 1000 (sq ft/sq meters) 
    and ensure friendly JSON messages are returned to the caller.
    <hr>
* [9.] (#task-9)
    Create documentation that is exposed in 
    Hypermedia Application Language (HAL) for the Room.area 
    field for fellow developers who will use your API 
    that explain that the value should represent either 
    square meters or square footage depending on your units of measure.
* [10.] (#task-10)
    Add a search resource that provides the ability 
    to find Devices based on a partial name.
    <hr>
* [11.] (#task-11)
    Track the last user to modify the control 
    and report it in control.lastModifiedBy
    <hr>
* [12.] (#task-12)
    Ensure that control modifications are versioned so 
    that clients can use appropriate E-Tags
    <hr>
* [13.] (#task-13)
    Add tests to validate all requested additional configuration
    <hr>
* [14.] (#task-14)
    Add additional searches to find rooms by name and 
    by less than a specified area
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
[ApplicationIntegrationTest]:
    ./src/test/java/com/teamtreehouse/home/ApplicationIntegrationTest.java "./src/test/java/com/teamtreehouse/home/ApplicationIntegrationTest.java"
    
[CustomUserDetailsService]:
    ./src/main/java/com/teamtreehouse/home/service/CustomUserDetailsService.java "./src/main/java/com/teamtreehouse/home/service/CustomUserDetailsService.java"
[ControlEventHandler]:
    ./src/main/java/com/teamtreehouse/home/handler/ControlEventHandler.java "./src/main/java/com/teamtreehouse/home/handler/ControlEventHandler.java"
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
    Build repositories that can be paged and sorted 
    for each of the entities.
    <hr>
    All repositories except from [UserDao] 
    are paged and sorted, i.e. *extend* 
    `PagingAndSortingRepository` from Spring Data:
    - [RoomDao]
    - [DeviceDao]
    - [ControlDao]

    [UserDao] *extends* `CrudRepository` because it is
    not REST exposed, so there is no need for it to
    be paged and/or sorted.
<hr>
4. <a id="task-4"></a>
    Expose the API under /api/v1/
    <hr>
    Property `spring.data.rest.basePath` is set to `/api/v1` in
    [application.properties] file.
<hr>
5. <a id="task-5"></a>
    Secure the API so that only authenticated users can 
    view details.
    <hr>
    1. Dependency 
    `org.springframework.boot:spring-boot-starter-security`
    is added to [build.gradle].
    2. [WebSecurityConfiguration] file configures security
    for our API. `configure` method ensures that every 
    request has to be authenticated with basic authentication.
<hr>
6. <a id="task-6"></a>
    Ensure that the users repository is not exported
    <hr>
    [UserDao] is marked with `@RepositoryRestResource(exported = false)`
    and thus is not exposed to REST API.
<hr>
7. <a id="task-7"></a>
    Secure the API so that only users with the ROLE_ADMIN role 
    can create rooms
    <hr>
    I added responsible @PreAuthorize annotation on top of
    `save` method in [RoomDao], that will check that user has
    'ROLE_ADMIN' before saving. In order to create room
    just like @craigsdennis does in Workshop using
    [DataLoader] `ApplicationRunner` class, I also
    added needed dependecy and authorized admin user in
    [DataLoader] to create couple of test rooms.
    <hr>
    I was not able to test this functionality properly.
    I can test that if user with "ROLE_ADMIN" creates
    room his request passes, see 
    `postMethodCreatingNewRoomShouldWorkWithAdminUser`
    in [ApplicationIntegrationTest]. 
    <br>
    Test trying to model situation where user with
    "ROLE_USER" is trying to create room, throws
    `NestedServletException` with nested 
    `AccessDeniedException` and not nice JSON with
    403 Error. And I don't know workaround. So I have test,
    that is trying to check that:
    `postMethodCreatingNewRoomShouldReturnAccessDeniedWithNormalUser`
    But it does not work properly. 
<hr>
8. <a id="task-8"><a/>
    Validate that room’s area is less than 1000 (sq ft/sq meters) 
    and ensure friendly JSON messages are returned to the caller.
    <hr>
    - In the [Room] class @Max annotation was added with 
    friendly message.
    - In the [RestConfig] class `ValidatingListener` was added to
    to "beforeSave" and "beforeCreate" events, so that when any 
    device, control or room are created and validation fails, 
    messages will be returned with JSON to user.
    <hr>
    Testing this in 
    `postMethodCreatingNewRoomWithBigAreaShouldReturnFriendlyError`
    in [ApplicationIntegrationTest].
<hr>
9. <a id="task-9"><a/>
    Create documentation that is exposed in 
    Hypermedia Application Language (HAL) for the Room.area 
    field for fellow developers who will use your API 
    that explain that the value should represent either 
    square meters or square footage depending on your units of measure.
    <hr>
    I've added [rest-messages.properties] file w descriptions to 
    all fields of room, device and control.
<hr>
10. <a id="task-10"><a/>
    Add a search resource that provides the ability 
    to find Devices based on a partial name.
    <hr>
    In [DeviceDao] `findByNameContaining` Spring Query method is
    defined. The implementation will be provided by Spring Data.
    <br>
    `@RestResource(rel = "name", path = "contains-name")`
    annotation is added to make the following query 
    available in browser:
    `BASE_URL/devices/search/contains-name?name=query`
    Where BASE_URL can be `localhost:8080/api/v1`, and
    "query" can be device name that we are looking for.
    <hr>
    This query is tested in `devicesCanBeSearchedByNameContaining()`
    test method in [ApplicationIntegrationTest]. 
<hr>
11. <a id="task-11"><a/>
    Track the last user to modify the control 
    and report it in control.lastModifiedBy
    <hr>
    For that purpose [ControlEventHandler] class was introduced
    marked with `@RepositoryEventHandler(Control.class` and
    handling @HandleBeforeCreate and @HandleBeforeSave events.
    When [Control] is saved or created, currently logged on
    user is added to `Control.lastModifiedBy` field.
    <br>
    Currently `Control.lastModifiedBy` is in @ManyToOne 
    relationship with [User]. Hope its right.
    <hr>
    Test checking this is method called:
    `afterCreationLoggedOnUserIsSetToLastModifiedByFieldInControl()`
    in [ApplicationIntegrationTest].    
<hr>
12. <a id="task-12"><a/>
    Ensure that control modifications are versioned 
    so that clients can use appropriate E-Tags.
    <hr>
    All Model classes [Control], [Device] and [Room] 
    are versioned, because they extend [BaseEntity]
    that has `private` field annotated with 
    @Version.
    <hr>
    Test checking "Etag" header for room detail
    page is situated in
    [ApplicationIntegrationTest] and is called:
    `roomDetailPageShouldHaveEtagHeader()`
<hr>
### Extra Credit
13. <a id="task-13"><a/>
    Add tests to validate all requested additional configuration
    <hr>
    Integration tests (or functional I guess) are available
    in [ApplicationIntegrationTest] and are called:
    - `getRequestToRoomsPageReturnsTwoRooms`:
        simple tests basically checking that GET request to rooms
        can be made and HATEOAS links and paging is there.
    - `postMethodCreatingNewRoomShouldWorkWithAdminUser`:
        test checking that Admin user can create new Room.
    - `postMethodCreatingNewRoomShouldReturnAccessDeniedWithNormalUser`:
        this test does not work, it throws `NestedServletException`
        with nested `AccessDeniedException`, but I decided to leave it
        for future TODO.       
    - `postMethodCreatingNewRoomWithBigAreaShouldReturnFriendlyError`:
        checks that creating new rooms with area more than 1000 
        returns friendly validation error.
    - `devicesCanBeSearchedByNameContaining`:
        checks that search request can be made and
        page with results is returned.
    - `afterCreationLoggedOnUserIsSetToLastModifiedByFieldInControl`:
        checks that after creation of Control, member 
        `Control.lastModifiedBy` is set to currently logged in User.
    - `roomDetailPageShouldHaveEtagHeader`:
       checks that room details page have "Etag" header.
<hr>
14. <a id="task-14"><a/>
    Add additional searches to find rooms by name and 
    by less than a specified area
    <hr>
<hr>
