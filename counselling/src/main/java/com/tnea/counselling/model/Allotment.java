package com.tnea.counselling.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Allotment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int studentId;
    //private String studentName;
    private String studentEmail;
    //private String studentPassword;
    //private int mathsMark;
    //private int secondMark;
    //private int thirdMark;
    private int cutoff ;
    private String studentCommunity;

    @Transient
    private List<Preference> preferences;
    private boolean isAlloted;
    private String allotedCollege;
    private String allotedCourse;

    public Allotment(){}

    public Allotment(Student student) {
        this.studentId = student.getId();
        this.studentEmail = student.getStudentEmail();
        this.cutoff = student.getCutoff();
        this.studentCommunity = student.getStudentCommunity();
        this.preferences = student.getPreferences();
        this.isAlloted = false;
    }
}
