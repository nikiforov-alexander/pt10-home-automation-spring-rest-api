package com.teamtreehouse.home.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @Min(0)
    private Integer area;

    @OneToMany(mappedBy = "room")
    private List<Device> devices = new ArrayList<>();

    //
    // Getters and Setters
    //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Room() {
        // default constructor for JPA
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
        this.devices.add(device);
    }
}
