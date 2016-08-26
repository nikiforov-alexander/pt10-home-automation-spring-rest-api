package com.teamtreehouse.home.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Control extends BaseEntity{
    @NotNull
    @NotEmpty
    private String name;
    // value of control: can be temperature value or 0/1 values for
    // switch
    private Integer value;

    // Many controls have one device
    @ManyToOne
    private Device device;

    //
    // Getters and Setters
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    //
    // Constructors
    //
    // default constructor for JPA
    // uses BaseEntity constructor
    protected Control() {
        super();
    }

    public Control(String name, Integer value) {
        this();
        this.name = name;
        this.value = value;
    }

}
