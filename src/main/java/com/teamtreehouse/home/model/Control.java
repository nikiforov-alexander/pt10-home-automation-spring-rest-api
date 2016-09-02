package com.teamtreehouse.home.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotNull
    private Integer value;

    // Many controls have one device
    @ManyToOne
    @NotNull(message = "Control cannot be created without Device")
    private Device device;

    // Many controls can be last modified by One User
    // Hope its right. Json Ignore, because all User related
    // fields should be ignored I think
    @JsonIgnore
    @ManyToOne
    private User lastModifiedBy;

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

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    //
    // Constructors
    //
    // default constructor for JPA
    // uses BaseEntity constructor
    public Control() {
        super();
    }

    public Control(String name, Integer value) {
        this();
        this.name = name;
        this.value = value;
    }
}
