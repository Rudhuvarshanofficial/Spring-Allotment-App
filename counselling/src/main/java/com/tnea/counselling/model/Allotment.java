
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

    private String studentEmail;

    private int cutoff;

    private String studentCommunity;

    private int meritRank;

    @Transient
    private List<Preference> preferences;

    private boolean isAlloted;

    private String allotedCollege;

    private String allotedCourse;

    private Integer allottedCollegeId;

    private Integer allottedCourseId;

    private Integer allottedPreferencePriority;

    private String allocationCategory;

    // Prevents a completed counselling run from being executed again
    private boolean counsellingProcessed;

    public Allotment() {
    }

    public Allotment(Student student) {
        this.studentId = student.getId();
        this.studentEmail = student.getStudentEmail();
        this.cutoff = student.getCutoff();
        this.studentCommunity = student.getStudentCommunity();
        this.preferences = student.getPreferences();
        this.isAlloted = false;
        this.counsellingProcessed = false;
    }

    public Allotment(Student student, int meritRank) {
        this(student);
        this.meritRank = meritRank;
    }
}
