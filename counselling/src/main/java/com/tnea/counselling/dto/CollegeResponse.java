
package com.tnea.counselling.dto;

import com.tnea.counselling.model.College;
import com.tnea.counselling.model.CollegeType;

import java.util.List;

public record CollegeResponse(
        Integer id,
        String collegeName,
        String collegeLocation,
        CollegeType collegeType,
        List<CourseResponse> course
) {
    public static CollegeResponse from(College college) {

        List<CourseResponse> courses = college.getCourse() == null
                ? List.of()
                : college.getCourse()
                .stream()
                .map(CourseResponse::from)
                .toList();

        return new CollegeResponse(
                college.getId(),
                college.getCollegeName(),
                college.getCollegeLocation(),
                college.getCollegeType(),
                courses
        );
    }
}
