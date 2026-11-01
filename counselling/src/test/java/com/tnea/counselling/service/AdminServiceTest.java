
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.CounsellingException;
import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.Allotment;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.StudentDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private AllotmentDao allotmentDao;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private AdminService adminService;

    private College college;

    @BeforeEach
    void setUp() {
        college = new College();
        college.setCollegeName("Test Engineering College");
    }

    @Test
    void shouldAllocateCommunitySeatWhenOcSeatsAreExhausted() {
        Course course = createCourse(101, "CSE", 0, 1);
        Preference preference = createPreference(1, 101);
        Student student = createStudent(1, "BC", List.of(preference));
        Allotment allotment = createAllotment(1, "BC", 1);

        mockStudentAndRanklist(student, allotment);
        when(courseDao.findByCourseAndCollegeId(101, 1)).thenReturn(course);

        String result = adminService.runCounselling();

        assertEquals("Counselling completed successfully", result);
        assertTrue(allotment.isAlloted());
        assertEquals("BC", allotment.getAllocationCategory());
        assertEquals(101, allotment.getAllottedCourseId());
        assertEquals(0, course.getRemainingBcSeats());
        assertEquals(0, course.getRemainingOcSeats());
        assertTrue(allotment.isCounsellingProcessed());

        verify(courseDao).save(course);
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldMoveToNextPreferenceWhenFirstPreferenceHasNoSeats() {
        Course firstCourse = createCourse(101, "CSE", 0, 0);
        Course secondCourse = createCourse(102, "IT", 1, 0);

        Preference firstPreference = createPreference(1, 101);
        Preference secondPreference = createPreference(2, 102);

        List<Preference> preferences = new ArrayList<>(
                List.of(secondPreference, firstPreference)
        );

        Student student = createStudent(2, "BC", preferences);
        Allotment allotment = createAllotment(2, "BC", 2);

        mockStudentAndRanklist(student, allotment);
        when(courseDao.findByCourseAndCollegeId(101, 1)).thenReturn(firstCourse);
        when(courseDao.findByCourseAndCollegeId(102, 1)).thenReturn(secondCourse);

        adminService.runCounselling();

        assertTrue(allotment.isAlloted());
        assertEquals("OC", allotment.getAllocationCategory());
        assertEquals(102, allotment.getAllottedCourseId());
        assertEquals(2, allotment.getAllottedPreferencePriority());
        assertEquals(0, secondCourse.getRemainingOcSeats());
        assertEquals(0, firstCourse.getRemainingOcSeats());
        assertTrue(allotment.isCounsellingProcessed());

        verify(courseDao).save(secondCourse);
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldNotAllocateCommunitySeatToOcStudent() {
        Course course = createCourse(101, "CSE", 0, 1);
        Preference preference = createPreference(1, 101);
        Student student = createStudent(3, "OC", List.of(preference));
        Allotment allotment = createAllotment(3, "OC", 3);

        mockStudentAndRanklist(student, allotment);
        when(courseDao.findByCourseAndCollegeId(101, 1)).thenReturn(course);

        adminService.runCounselling();

        assertFalse(allotment.isAlloted());
        assertNull(allotment.getAllocationCategory());
        assertEquals(1, course.getRemainingBcSeats());
        assertEquals(0, course.getRemainingOcSeats());
        assertTrue(allotment.isCounsellingProcessed());

        verify(courseDao, never()).save(any(Course.class));
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldRemainUnallottedWhenAllPreferencesAreExhausted() {
        Course firstCourse = createCourse(101, "CSE", 0, 0);
        Course secondCourse = createCourse(102, "IT", 0, 0);

        Preference firstPreference = createPreference(1, 101);
        Preference secondPreference = createPreference(2, 102);

        Student student = createStudent(
                4, "BC", List.of(firstPreference, secondPreference)
        );
        Allotment allotment = createAllotment(4, "BC", 4);

        mockStudentAndRanklist(student, allotment);
        when(courseDao.findByCourseAndCollegeId(101, 1)).thenReturn(firstCourse);
        when(courseDao.findByCourseAndCollegeId(102, 1)).thenReturn(secondCourse);

        adminService.runCounselling();

        assertFalse(allotment.isAlloted());
        assertNull(allotment.getAllotedCollege());
        assertNull(allotment.getAllotedCourse());
        assertNull(allotment.getAllocationCategory());
        assertTrue(allotment.isCounsellingProcessed());

        verify(courseDao, never()).save(any(Course.class));
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldRejectCounsellingWhenAlreadyProcessed() {
        Allotment allotment = createAllotment(5, "BC", 1);
        allotment.setCounsellingProcessed(true);

        when(allotmentDao.findAllByOrderByMeritRankAsc())
                .thenReturn(List.of(allotment));

        CounsellingException exception = assertThrows(
                CounsellingException.class,
                () -> adminService.runCounselling()
        );

        assertEquals(
                "Counselling has already been completed. "
                        + "Create a fresh counselling dataset to run again.",
                exception.getMessage()
        );

        verifyNoInteractions(studentDao, courseDao);
        verify(allotmentDao, never()).saveAll(anyList());
    }

    @Test
    void shouldAllocateSeatInMeritRankOrder() {
        Course course = createCourse(101, "CSE", 1, 0);

        Preference firstPreference = createPreference(1, 101);
        Preference secondPreference = createPreference(1, 101);

        Student firstStudent = createStudent(
                6, "BC", List.of(firstPreference)
        );

        Student secondStudent = createStudent(
                7, "BC", List.of(secondPreference)
        );

        Allotment firstAllotment = createAllotment(6, "BC", 1);
        Allotment secondAllotment = createAllotment(7, "BC", 2);

        when(allotmentDao.findAllByOrderByMeritRankAsc())
                .thenReturn(List.of(firstAllotment, secondAllotment));

        when(studentDao.findById(6))
                .thenReturn(Optional.of(firstStudent));

        when(studentDao.findById(7))
                .thenReturn(Optional.of(secondStudent));

        when(courseDao.findByCourseAndCollegeId(101, 1))
                .thenReturn(course);

        adminService.runCounselling();

        assertTrue(firstAllotment.isAlloted());
        assertEquals("OC", firstAllotment.getAllocationCategory());

        assertFalse(secondAllotment.isAlloted());
        assertNull(secondAllotment.getAllocationCategory());

        assertEquals(0, course.getRemainingOcSeats());

        assertTrue(firstAllotment.isCounsellingProcessed());
        assertTrue(secondAllotment.isCounsellingProcessed());

        verify(courseDao, times(1)).save(course);
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldRejectInvalidCommunityWithoutConsumingSeats() {
        Course course = createCourse(101, "CSE", 1, 1);
        Preference preference = createPreference(1, 101);

        Student student = createStudent(
                8, "XYZ", List.of(preference)
        );

        Allotment allotment = createAllotment(8, "XYZ", 1);

        mockStudentAndRanklist(student, allotment);

        CounsellingException exception = assertThrows(
                CounsellingException.class,
                () -> adminService.runCounselling()
        );

        assertEquals(
                "Unsupported student community: XYZ",
                exception.getMessage()
        );

        assertEquals(1, course.getRemainingOcSeats());
        assertEquals(1, course.getRemainingBcSeats());

        assertFalse(allotment.isAlloted());
        assertFalse(allotment.isCounsellingProcessed());

        verifyNoInteractions(courseDao);
        verify(allotmentDao, never()).saveAll(anyList());
    }

    @Test
    void shouldRejectAllotmentWhenStudentRecordIsMissing() {

        Allotment allotment = createAllotment(9, "BC", 1);

        when(allotmentDao.findAllByOrderByMeritRankAsc())
                .thenReturn(List.of(allotment));

        when(studentDao.findById(9))
                .thenReturn(Optional.empty());

        assertThrows(
                NoRecordException.class,
                () -> adminService.runCounselling()
        );

        assertFalse(allotment.isAlloted());
        assertFalse(allotment.isCounsellingProcessed());

        verify(studentDao).findById(9);
        verifyNoInteractions(courseDao);
        verify(allotmentDao, never()).saveAll(anyList());
    }

    @Test
    void shouldSkipMissingCourseAndMoveToNextPreference() {

        Course secondCourse = createCourse(102, "IT", 1, 0);

        Preference firstPreference = createPreference(1, 101);
        Preference secondPreference = createPreference(2, 102);

        Student student = createStudent(
                10,
                "BC",
                List.of(firstPreference, secondPreference)
        );

        Allotment allotment = createAllotment(10, "BC", 1);

        mockStudentAndRanklist(student, allotment);

        when(courseDao.findByCourseAndCollegeId(101, 1))
                .thenReturn(null);

        when(courseDao.findByCourseAndCollegeId(102, 1))
                .thenReturn(secondCourse);

        adminService.runCounselling();

        assertTrue(allotment.isAlloted());
        assertEquals("IT", allotment.getAllotedCourse());
        assertEquals(102, allotment.getAllottedCourseId());
        assertEquals(2, allotment.getAllottedPreferencePriority());
        assertEquals("OC", allotment.getAllocationCategory());

        assertEquals(0, secondCourse.getRemainingOcSeats());
        assertTrue(allotment.isCounsellingProcessed());

        verify(courseDao).save(secondCourse);
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldRemainUnallottedWhenStudentHasNoPreferences() {

        Student student = createStudent(
                11,
                "BC",
                List.of()
        );

        Allotment allotment = createAllotment(11, "BC", 1);

        mockStudentAndRanklist(student, allotment);

        String result = adminService.runCounselling();

        assertEquals("Counselling completed successfully", result);

        assertFalse(allotment.isAlloted());
        assertNull(allotment.getAllotedCollege());
        assertNull(allotment.getAllotedCourse());
        assertNull(allotment.getAllocationCategory());

        assertTrue(allotment.isCounsellingProcessed());

        verifyNoInteractions(courseDao);
        verify(allotmentDao).saveAll(anyList());
    }

    @Test
    void shouldRemainUnallottedWhenPreferenceListIsNull() {

        Student student = createStudent(12, "BC", null);

        Allotment allotment = createAllotment(12, "BC", 1);

        mockStudentAndRanklist(student, allotment);

        String result = adminService.runCounselling();

        assertEquals("Counselling completed successfully", result);

        assertFalse(allotment.isAlloted());
        assertNull(allotment.getAllotedCollege());
        assertNull(allotment.getAllotedCourse());
        assertNull(allotment.getAllocationCategory());

        assertTrue(allotment.isCounsellingProcessed());

        verifyNoInteractions(courseDao);
        verify(allotmentDao).saveAll(anyList());
    }

    private Course createCourse(
            int courseId, String courseName, int ocSeats, int bcSeats) {

        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setCollege(college);
        course.setRemainingOcSeats(ocSeats);
        course.setRemainingBcSeats(bcSeats);

        return course;
    }

    private Preference createPreference(int priority, int courseId) {
        Preference preference = new Preference();
        preference.setPriority(priority);
        preference.setCollegeid(1);
        preference.setCourseid(courseId);
        preference.setCollege(college);

        return preference;
    }

    private Student createStudent(
            int studentId,
            String community,
            List<Preference> preferences) {

        Student student = new Student();
        student.setId(studentId);
        student.setStudentName("Test Student");
        student.setStudentEmail("student" + studentId + "@example.com");
        student.setStudentCommunity(community);

        student.setPreferences(
                preferences == null
                        ? null
                        : new ArrayList<>(preferences)
        );

        return student;
    }

    private Allotment createAllotment(
            int studentId, String community, int meritRank) {

        Allotment allotment = new Allotment();
        allotment.setStudentId(studentId);
        allotment.setStudentCommunity(community);
        allotment.setMeritRank(meritRank);
        allotment.setAlloted(false);
        allotment.setCounsellingProcessed(false);

        return allotment;
    }

    private void mockStudentAndRanklist(
            Student student, Allotment allotment) {

        when(allotmentDao.findAllByOrderByMeritRankAsc())
                .thenReturn(List.of(allotment));

        when(studentDao.findById(student.getId()))
                .thenReturn(Optional.of(student));
    }
}
