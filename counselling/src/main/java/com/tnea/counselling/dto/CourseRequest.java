
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
        @Min(value = 0, message = "Total seats cannot be negative")
        Integer totalSeats,

        @NotNull(message = "BC seats are required")
        @Min(value = 0, message = "BC seats cannot be negative")
        Integer bcSeats,

        @NotNull(message = "SC seats are required")
        @Min(value = 0, message = "SC seats cannot be negative")
        Integer scSeats,

        @NotNull(message = "MBC seats are required")
        @Min(value = 0, message = "MBC seats cannot be negative")
        Integer mbcSeats,

        @NotNull(message = "OC seats are required")
        @Min(value = 0, message = "OC seats cannot be negative")
        Integer ocSeats,

        @NotNull(message = "ST seats are required")
        @Min(value = 0, message = "ST seats cannot be negative")
        Integer stSeats
) {
}
