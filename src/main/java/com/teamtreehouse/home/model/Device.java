package com.teamtreehouse.home.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Device extends BaseEntity{

    @NotNull
    @NotEmpty
    private String name;

    // many controls will be in one device, and this is mapped by device
    // by foreign key in devices table
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Control> controls;

    // many devices will be in one room
    @ManyToOne
    private Room room;

    //
    // Getters and Setters
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


    //
    // Constructors
    //

    // default constructor for JPA
    // calls BaseEntity constructor
    public Device() {
        super();
        controls = new ArrayList<>();
    }

    public Device(String name) {
        this();
        this.name = name;
    }

    public Device(String name, List<Control> controls) {
        this();
        this.name = name;
        this.controls = controls;
    }

    // method adding control to device
    public void addControl(Control control) {
        // set control's device to this device
        control.setDevice(this);
        // add control
        controls.add(control);
    }
}
