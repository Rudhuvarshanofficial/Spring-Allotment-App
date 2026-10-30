
package com.tnea.counselling.dto;

import com.tnea.counselling.model.Course;

public record CourseResponse(
        Integer id,
        Integer courseId,
        String courseName,
        Integer totalSeats,
        Integer bcSeats,
        Integer scSeats,
        Integer mbcSeats,
        Integer ocSeats,
        Integer stSeats
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getCourseId(),
                course.getCourseName(),
                course.getTotalSeats(),
                course.getBcSeats(),
                course.getScSeats(),
                course.getMbcSeats(),
                course.getOcSeats(),
                course.getStSeats()
        );
    }
}
