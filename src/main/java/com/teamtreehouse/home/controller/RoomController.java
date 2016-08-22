package com.teamtreehouse.home.controller;

import com.teamtreehouse.home.model.Room;
import com.teamtreehouse.home.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
// produces variable in annotation below helps to resolve:
// unable to marshal type error:
// http://stackoverflow.com/questions/23480517/spring-hateoas-w-spring-boot-jaxb-marshal-error-when-returning-a-resourcest
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {
    @Autowired
    private RoomService roomService;

//    // list all rooms page
//    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
//    public List<Resource<Room>> getAllRooms(){
//        // get all rooms from db
//        List<Room> rooms = roomService.findAll();
//
//        // create new List with Room resources, to add
//        return rooms.stream()
//               .map(
//                    room -> methodOn(RoomController.class)
//                            .getRoomResources(room)
//                )
//                .collect(Collectors.toList());
//    }
    // list all rooms page
    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public List<Room> getAllRooms(){
        // get all rooms from db
        List<Room> rooms = roomService.findAll();
        // list of rooms with links
        List<Room> roomsWithLinks = new ArrayList<>();
        // create new List with Room resources, to add
        for (Room room: rooms) {
            room.add(
                    linkTo(
                            methodOn(RoomController.class)
                                    .getRoom(room.getPrimaryKey())
                    ).withSelfRel()
            );
            roomsWithLinks.add(room);
        }
        return roomsWithLinks;
    }

//    // show room with id
//    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET)
//    public Resource<Room> getRoom(
//            @PathVariable(value = "id") Long id) {
//        // find room in db by id
//        Room room = roomService.findOne(id);
//        // convert room by adding HATEOAS links
//        return getRoomResources(room);
//    }

    // show room with id
    @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET)
    public Room getRoom(
            @PathVariable(value = "id") Long id) {
        // find room in db by id
        Room room = roomService.findOne(id);
        // convert room by adding HATEOAS links
        room.add(
                linkTo(
                        methodOn(RoomController.class)
                                .getRoom(room.getPrimaryKey())
                ).withSelfRel()
        );
        return room;
    }

    /**
     * Converts room from db by adding proper links:
     * - self link with address
     * -
     * @param room - room to be converted into Resource<Room>
     * @return Resource<Room>
     */
//    private Resource<Room> getRoomResources(Room room) {
//        Resource<Room> resource = new Resource<>(room);
//        room.add(
//                linkTo(
//                        methodOn(RoomController.class)
//                                .getRoom(room.getPrimaryKey())
//                ).withSelfRel()
//        );
//        resource.add(
//                linkTo(
//                        methodOn(RoomController.class)
//                                .getRoom(room.getPrimaryKey())
//                ).withSelfRel()
//        );
//        return resource;
//    }
//    private Resource<Room> getRoomResources(Room room) {
//        Resource<Room> resource = new Resource<>(room);
//        room.add(
//                linkTo(
//                        methodOn(RoomController.class)
//                                .getRoom(room.getPrimaryKey())
//                ).withSelfRel()
//        );
//        resource.add(
//                linkTo(
//                        methodOn(RoomController.class)
//                                .getRoom(room.getPrimaryKey())
//                ).withSelfRel()
//        );
//        return resource;
//    }
}
