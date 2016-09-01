package com.teamtreehouse.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room extends BaseEntity {
    @NotNull
    @NotEmpty
    private String name;

    @Min(0)
    @Max(value = 1000, message = "Room area has to be less than 1000 sq.m.")
    private Integer area;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Device> devices;

    @JsonIgnore
    // I'll let this to be EAGER because I'm unfortunately do not
    // know how to instantiate lazily room.administrators :(
    // this is definitely one of the things to do in future
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> administrators;

    //
    // Getters and Setters
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<User> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<User> administrators) {
        this.administrators = administrators;
    }


    // default constructor for JPA,
    // calls BaseEntity constructor
    public Room() {
        super();
        devices = new ArrayList<>();
        administrators = new ArrayList<>();
    }

    public Room(String name, int area, List<Device> devices) {
        this();
        this.name = name;
        this.area = area;
        this.devices = devices;
    }

    public Room(String name, int area) {
        this();
        this.name = name;
        this.area = area;
    }

    // add device used in DataLoader
    public void addDevice(Device device) {
        // set device's room to this room
        device.setRoom(this);
        // add device
        this.devices.add(device);
    }

    // add user to Room administrators
    public void addUserToRoomAdministrators(User user) {
        this.administrators.add(user);
    }

    /**
     * Method checking whether this Room.administrators
     * has object passed as
     * argument and casted to com.teamtreehouse.model.User
     * @param object: Principal object returned from
     *              authentication.principal in @PreAuthorize in
     *              RoomDao.save method. It is actually our
     *              com.teamtreehouse.model.User object, but passed
     *              as Object
     * @return true: if Room.administrators contain passed User
     *         false: otherwise
     */
    public boolean hasAdministrator(Object object) {
        User user = (User) object;
        System.out.println("printing room admins: "
                + administrators);
        return administrators.contains(user);
    }
}
