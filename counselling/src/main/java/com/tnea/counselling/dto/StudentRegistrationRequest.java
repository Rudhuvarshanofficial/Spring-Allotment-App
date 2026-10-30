
package com.tnea.counselling.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentRegistrationRequest(

        @NotBlank(message = "Student name is required")
        @Size(max = 100, message = "Student name must not exceed 100 characters")
        String studentName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String studentEmail,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72,
                message = "Password must contain between 8 and 72 characters")
        String studentPassword,

        @NotNull(message = "Maths mark is required")
        @Min(value = 0, message = "Maths mark cannot be below 0")
        @Max(value = 100, message = "Maths mark cannot exceed 100")
        Integer mathsMark,

        @NotNull(message = "Second subject mark is required")
        @Min(value = 0, message = "Second subject mark cannot be below 0")
        @Max(value = 100, message = "Second subject mark cannot exceed 100")
        Integer secondMark,

        @NotNull(message = "Third subject mark is required")
        @Min(value = 0, message = "Third subject mark cannot be below 0")
        @Max(value = 100, message = "Third subject mark cannot exceed 100")
        Integer thirdMark,

        @NotBlank(message = "Community is required")
        String studentCommunity

) {
}
