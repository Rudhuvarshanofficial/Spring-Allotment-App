
package com.tnea.counselling.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(

        @NotNull(message = "Course ID is required")
        @Min(value = 1, message = "Course ID must be at least 1")
        Integer courseId,

        @NotBlank(message = "Course name is required")
        String courseName,

        @NotNull(message = "Total seats are required")
        @Min(value = 1, message = "Total seats must be at least 1")
        Integer totalSeats
) {
}
