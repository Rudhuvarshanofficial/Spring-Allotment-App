
package com.tnea.counselling.dto;

import com.tnea.counselling.model.Course;

public record CourseResponse(
        Integer id,
        Integer courseId,
        String courseName,
        Integer totalSeats,
        Integer counsellingSeats,
        Integer ocSeats,
        Integer bcSeats,
        Integer bcmSeats,
        Integer mbcSeats,
        Integer scSeats,
        Integer scaSeats,
        Integer stSeats
) {
    public static CourseResponse from(Course course) {

        return new CourseResponse(
                course.getId(),
                course.getCourseId(),
                course.getCourseName(),
                course.getTotalSeats(),
                course.getCounsellingSeats(),
                course.getOcSeats(),
                course.getBcSeats(),
                course.getBcmSeats(),
                course.getMbcSeats(),
                course.getScSeats(),
                course.getScaSeats(),
                course.getStSeats()
        );
    }
}
