
package com.tnea.counselling.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PreferenceRequest(

        @NotNull(message = "Priority is required")
        @Min(value = 1, message = "Priority must be at least 1")
        Integer priority,

        @NotNull(message = "College ID is required")
        @Min(value = 1, message = "College ID must be at least 1")
        Integer collegeid,

        @NotNull(message = "Course ID is required")
        @Min(value = 1, message = "Course ID must be at least 1")
        Integer courseid

) {
}
