
package com.tnea.counselling.dto;

import com.tnea.counselling.model.Student;

public record StudentResponse(
        Integer id,
        String studentName,
        String studentEmail,
        Integer mathsMark,
        Integer secondMark,
        Integer thirdMark,
        Integer cutoff,
        String studentCommunity
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getStudentName(),
                student.getStudentEmail(),
                student.getMathsMark(),
                student.getSecondMark(),
                student.getThirdMark(),
                student.getCutoff(),
                student.getStudentCommunity()
        );
    }
}
