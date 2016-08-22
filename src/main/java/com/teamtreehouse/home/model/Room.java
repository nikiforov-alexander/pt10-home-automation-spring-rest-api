package com.teamtreehouse.home.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Room extends ResourceSupport{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @Min(0)
    private Integer squareFootage;

    //
    // Getters and Setters
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public Room() {
        // default constructor for JPA
    }

    @JsonCreator
    public Room(
            @JsonProperty("name") String name,
            @JsonProperty("squareFootage") Integer squareFootage) {
        this.name = name;
        this.squareFootage = squareFootage;
    }

    public Long getPrimaryKey() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
