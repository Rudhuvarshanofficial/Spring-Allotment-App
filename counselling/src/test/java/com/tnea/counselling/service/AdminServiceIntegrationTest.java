
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.CounsellingException;
import com.tnea.counselling.model.Allotment;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.CourseDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:mysql://localhost:3306/counselling_integration_test"
        }
)
class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private AllotmentDao allotmentDao;

    @Test
    void shouldRollbackAllChangesWhenLaterStudentHasInvalidCommunity() {

        // Verify the initial course state.
        Course courseBefore = courseDao.findById(1)
                .orElseThrow();

        assertEquals(1, courseBefore.getRemainingOcSeats());
        assertEquals(1, courseBefore.getRemainingBcSeats());

        // Verify initial allotment states.
        Allotment firstBefore = allotmentDao.findByStudentId(1);
        Allotment secondBefore = allotmentDao.findByStudentId(2);

        assertNotNull(firstBefore);
        assertNotNull(secondBefore);

        assertFalse(firstBefore.isAlloted());
        assertFalse(secondBefore.isAlloted());

        assertFalse(firstBefore.isCounsellingProcessed());
        assertFalse(secondBefore.isCounsellingProcessed());

        // Student A gets the OC seat first.
        // Student B then causes an exception due to community XYZ.
        assertThrows(
                CounsellingException.class,
                () -> adminService.runCounselling()
        );

        // Read the database again after the failed transaction.
        Course courseAfter = courseDao.findById(1)
                .orElseThrow();

        Allotment firstAfter = allotmentDao.findByStudentId(1);
        Allotment secondAfter = allotmentDao.findByStudentId(2);

        // Verify that the seat decrement was rolled back.
        assertEquals(1, courseAfter.getRemainingOcSeats());
        assertEquals(1, courseAfter.getRemainingBcSeats());

        // Verify that Student A's allotment was rolled back.
        assertFalse(firstAfter.isAlloted());
        assertNull(firstAfter.getAllocationCategory());
        assertNull(firstAfter.getAllotedCourse());

        // Verify that Student B remains unchanged.
        assertFalse(secondAfter.isAlloted());
        assertNull(secondAfter.getAllocationCategory());

        // Neither student should be marked as processed.
        assertFalse(firstAfter.isCounsellingProcessed());
        assertFalse(secondAfter.isCounsellingProcessed());
    }
}
