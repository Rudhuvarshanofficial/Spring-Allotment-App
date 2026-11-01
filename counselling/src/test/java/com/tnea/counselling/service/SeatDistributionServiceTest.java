
package com.tnea.counselling.service;

import com.tnea.counselling.model.CollegeType;
import com.tnea.counselling.model.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatDistributionServiceTest {

    private final SeatDistributionService service =
            new SeatDistributionService();

    @Test
    void shouldDistributeGovernmentSeatsCorrectly() {

        Course course = createCourse(120);

        service.distributeSeats(course, CollegeType.GOVERNMENT);

        assertEquals(120, course.getCounsellingSeats());

        assertEquals(37, course.getOcSeats());
        assertEquals(32, course.getBcSeats());
        assertEquals(4, course.getBcmSeats());
        assertEquals(24, course.getMbcSeats());
        assertEquals(18, course.getScSeats());
        assertEquals(4, course.getScaSeats());
        assertEquals(1, course.getStSeats());

        assertCategoryTotal(course, 120);
    }

    @Test
    void shouldRoundPrivateSeatsUp() {

        Course course = createCourse(101);

        service.distributeSeats(course, CollegeType.PRIVATE);

        assertEquals(51, course.getCounsellingSeats());

        assertEquals(16, course.getOcSeats());
        assertEquals(13, course.getBcSeats());
        assertEquals(2, course.getBcmSeats());
        assertEquals(10, course.getMbcSeats());
        assertEquals(8, course.getScSeats());
        assertEquals(2, course.getScaSeats());
        assertEquals(0, course.getStSeats());

        assertCategoryTotal(course, 51);
    }

    @Test
    void shouldHandleOneSeatCorrectly() {

        Course course = createCourse(1);

        service.distributeSeats(course, CollegeType.GOVERNMENT);

        assertEquals(1, course.getCounsellingSeats());
        assertEquals(1, course.getOcSeats());

        assertCategoryTotal(course, 1);
    }

    @Test
    void shouldDistributeTenSeatsWithoutLosingSeats() {

        Course course = createCourse(10);

        service.distributeSeats(course, CollegeType.GOVERNMENT);

        assertEquals(3, course.getOcSeats());
        assertEquals(3, course.getBcSeats());
        assertEquals(0, course.getBcmSeats());
        assertEquals(2, course.getMbcSeats());
        assertEquals(2, course.getScSeats());
        assertEquals(0, course.getScaSeats());
        assertEquals(0, course.getStSeats());

        assertCategoryTotal(course, 10);
    }

    @Test
    void shouldDistributeOneThousandSeatsCorrectly() {

        Course course = createCourse(1000);

        service.distributeSeats(course, CollegeType.GOVERNMENT);

        assertEquals(310, course.getOcSeats());
        assertEquals(265, course.getBcSeats());
        assertEquals(35, course.getBcmSeats());
        assertEquals(200, course.getMbcSeats());
        assertEquals(150, course.getScSeats());
        assertEquals(30, course.getScaSeats());
        assertEquals(10, course.getStSeats());

        assertCategoryTotal(course, 1000);
    }

    private Course createCourse(int totalSeats) {

        Course course = new Course();
        course.setTotalSeats(totalSeats);

        return course;
    }

    private void assertCategoryTotal(
            Course course,
            int expectedTotal) {

        int actualTotal =
                course.getOcSeats()
                        + course.getBcSeats()
                        + course.getBcmSeats()
                        + course.getMbcSeats()
                        + course.getScSeats()
                        + course.getScaSeats()
                        + course.getStSeats();

        assertEquals(expectedTotal, actualTotal);
    }
}
