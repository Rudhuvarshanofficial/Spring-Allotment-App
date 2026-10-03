package com.tnea.counselling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(1)
    private int courseId;

    @NotNull
    private String courseName;

    @Min(0)
    private int totalSeats;
    @Min(0)
    private int bcSeats;
    @Min(0)
    private int scSeats;
    @Min(0)
    private int mbcSeats;
    @Min(0)
    private int ocSeats;
    @Min(0)
    private int stSeats;
    @ManyToOne
    @JsonIgnore
    private College college;



}
