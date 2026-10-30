
package com.tnea.counselling.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String collegeName;

    private String collegeLocation;

    @Enumerated(EnumType.STRING)
    private CollegeType collegeType;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL)
    private List<Course> course;
}
