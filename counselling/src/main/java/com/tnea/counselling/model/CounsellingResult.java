
package com.tnea.counselling.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "counselling_result",
        indexes = {
                @Index(name = "idx_result_run_id", columnList = "run_id")
        }
)
public class CounsellingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identifies a particular counselling execution
    @Column(name = "run_id", nullable = false)
    private String runId;

    // Original operational allotment ID.
    // Intentionally not a foreign key.
    private Integer originalAllotmentId;

    // Student details
    private Integer studentId;

    private String studentName;

    private String studentEmail;

    private int cutoff;

    private int meritRank;

    private String community;

    // ALLOTTED or UNALLOTTED
    @Column(name = "result_status", nullable = false)
    private String status;

    // Allotment details (null when unallotted)
    private String collegeName;

    private String courseName;

    private String allocationCategory;

    private Integer preferencePriority;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreationTime() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
