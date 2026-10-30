
package com.tnea.counselling.dto;

import com.tnea.counselling.model.CollegeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CollegeRequest(

        @NotBlank(message = "College name is required")
        String collegeName,

        @NotBlank(message = "College location is required")
        String collegeLocation,

        @NotNull(message = "College type is required")
        CollegeType collegeType,

        @NotEmpty(message = "At least one course is required")
        List<@Valid CourseRequest> course
) {
}
