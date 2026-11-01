
package com.tnea.counselling.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    // Original sanctioned intake
    @Min(1)
    @Column(nullable = false)
    private int totalSeats;

    // Original counselling intake
    @Min(0)
    @Column(nullable = false)
    private int counsellingSeats;

    // Original category-wise distribution
    @Min(0)
    @Column(nullable = false)
    private int ocSeats;

    @Min(0)
    @Column(nullable = false)
    private int bcSeats;

    @Min(0)
    @Column(nullable = false)
    private int bcmSeats;

    @Min(0)
    @Column(nullable = false)
    private int mbcSeats;

    @Min(0)
    @Column(nullable = false)
    private int scSeats;

    @Min(0)
    @Column(nullable = false)
    private int scaSeats;

    @Min(0)
    @Column(nullable = false)
    private int stSeats;

    // Remaining seats available during counselling
    @Min(0)
    @Column(nullable = false)
    private int remainingOcSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingBcSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingBcmSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingMbcSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingScSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingScaSeats;

    @Min(0)
    @Column(nullable = false)
    private int remainingStSeats;

    @ManyToOne
    @JsonIgnore
    private College college;
}
