package com.tnea.counselling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private int studentId;
    //private int collegeId;
    //private int courseId;
    @Min(1)
    private int priority;
    @Min(1)
    private int collegeid;
    @Min(1)
    private int courseid;

    @ManyToOne
    @JsonIgnore
    private College college;

    @ManyToOne
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JsonIgnore
    private Student student;
}
