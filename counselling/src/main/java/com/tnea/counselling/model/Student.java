package com.tnea.counselling.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String studentName;
    @Email
    private String studentEmail;
    @NotNull
    private String studentPassword;
    @Min(0)
    @Max(100)
    private int mathsMark;
    @Min(0)
    @Max(100)
    private int secondMark;
    @Min(0)
    @Max(100)
    private int thirdMark;
    private int cutoff ;
    @NotNull
    private String studentCommunity;
    @OneToMany(mappedBy = "student")
    private List<Preference> preferences;

}
