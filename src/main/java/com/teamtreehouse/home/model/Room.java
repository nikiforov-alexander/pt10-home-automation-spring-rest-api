package com.teamtreehouse.home.model;

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

    //
    // Getters and Setters
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    // default constructor for JPA,
    // calls BaseEntity constructor
    protected Room() {
        super();
        devices = new ArrayList<>();
    }

    public Room(String name, int area, List<Device> devices) {
        this.name = name;
        this.area = area;
        this.devices = devices;
    }

    public Room(String name, int area) {
        this.name = name;
        this.area = area;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    // add device used in DataLoader
    public void addDevice(Device device) {
        // set device's room to this room
        device.setRoom(this);
        // add device
        this.devices.add(device);
    }
}
