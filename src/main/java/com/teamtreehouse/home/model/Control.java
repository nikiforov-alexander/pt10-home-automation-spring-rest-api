package com.teamtreehouse.home.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private Integer value;

    // Many controls have one device
    @ManyToOne
    private Device device;


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



    public Control(String name) {
        this.name = name;
    }

    public Control(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public Control() {
        // default constructor for JPA
    }
}
