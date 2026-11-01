
package com.tnea.counselling.service;

import com.tnea.counselling.model.*;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.CollegeDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.PreferenceDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DemoDataService {

    private final PreferenceDao preferenceDao;
    private final AllotmentDao allotmentDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final CollegeDao collegeDao;
    private final SeatDistributionService seatDistributionService;
    private final AllotmentService allotmentService;

    @Value("${app.demo.enabled:false}")
    private boolean demoEnabled;

    private static final int TOTAL_STUDENTS = 120;

    private static final Community[] COMMUNITIES = {
            Community.OC,
            Community.BC,
            Community.BCM,
            Community.MBC,
            Community.SC,
            Community.SCA,
            Community.ST
    };

    private static final int[] COMMUNITY_COUNTS = {
            15, 30, 10, 25, 25, 10, 5
    };

    public DemoDataService(
            PreferenceDao preferenceDao,
            AllotmentDao allotmentDao,
            StudentDao studentDao,
            CourseDao courseDao,
            CollegeDao collegeDao,
            SeatDistributionService seatDistributionService,
            AllotmentService allotmentService) {

        this.preferenceDao = preferenceDao;
        this.allotmentDao = allotmentDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.collegeDao = collegeDao;
        this.seatDistributionService = seatDistributionService;
        this.allotmentService = allotmentService;
    }

    private void ensureDemoEnabled() {
        if (!demoEnabled) {
            throw new IllegalStateException(
                    "Demo operations are disabled."
            );
        }
    }

    @Transactional
    public String resetDemoData() {

        ensureDemoEnabled();

        long preferences = preferenceDao.count();
        long allotments = allotmentDao.count();
        long students = studentDao.count();
        long courses = courseDao.count();
        long colleges = collegeDao.count();

        preferenceDao.deleteAllInBatch();
        allotmentDao.deleteAllInBatch();
        studentDao.deleteAllInBatch();
        courseDao.deleteAllInBatch();
        collegeDao.deleteAllInBatch();

        return "Demo data reset completed successfully. "
                + "Deleted: "
                + preferences + " preferences, "
                + allotments + " allotments, "
                + students + " students, "
                + courses + " courses, "
                + colleges + " colleges. "
                + "Historical counselling results and user accounts were preserved.";
    }

    @Transactional
    public String generateDemoData() {

        ensureDemoEnabled();

        // Prevent duplicate demo dataset generation.
        if (studentDao.count() > 0
                || collegeDao.count() > 0
                || courseDao.count() > 0
                || preferenceDao.count() > 0
                || allotmentDao.count() > 0) {

            throw new IllegalStateException(
                    "Operational data already exists. "
                            + "Reset demo data before generating a new dataset."
            );
        }

        // 1. Create the demo college.
        College college = new College();

        college.setCollegeName("Demo Engineering College");
        college.setCollegeLocation("Chennai, Tamil Nadu");
        college.setCollegeType(CollegeType.PRIVATE);

        // 2. Create three courses with the requested capacities.
        List<Course> courses = createDemoCourses(college);

        college.setCourse(courses);

        // Saving the college also saves its courses through cascade.
        College savedCollege = collegeDao.save(college);

        // 3. Generate 120 students.
        List<String> communitySequence =
                generateCommunitySequence();

        List<Student> students = new ArrayList<>();

        // One encoded placeholder password for demo student records.
        // No corresponding authentication accounts are created.
        String placeholderPassword =
                new BCryptPasswordEncoder(12)
                        .encode(UUID.randomUUID().toString());

        for (int i = 0; i < TOTAL_STUDENTS; i++) {

            Student student = new Student();

            int subjectMark =
                    100 - (i * 100 / (TOTAL_STUDENTS - 1));

            student.setStudentName(
                    String.format("Demo Student %03d", i + 1)
            );

            student.setStudentEmail(
                    String.format(
                            "demo.student%03d@example.com",
                            i + 1
                    )
            );

            student.setStudentPassword(placeholderPassword);

            student.setMathsMark(100);
            student.setSecondMark(subjectMark);
            student.setThirdMark(subjectMark);

            int cutoff = 100 + subjectMark;

            student.setCutoff(cutoff);

            student.setStudentCommunity(
                    communitySequence.get(i)
            );

            students.add(student);
        }

        List<Student> savedStudents =
                studentDao.saveAll(students);

        // 4. Generate three preferences for every student.
        List<Preference> preferences = new ArrayList<>();

        for (int i = 0; i < savedStudents.size(); i++) {

            Student student = savedStudents.get(i);

            for (int priority = 1; priority <= 3; priority++) {

                int courseIndex =
                        (i + priority - 1) % courses.size();

                Course selectedCourse = courses.get(courseIndex);

                Preference preference = new Preference();

                preference.setPriority(priority);

                preference.setCollegeid(savedCollege.getId());

                preference.setCourseid(
                        selectedCourse.getCourseId()
                );

                preference.setCollege(savedCollege);
                preference.setCourse(selectedCourse);
                preference.setStudent(student);

                preferences.add(preference);
            }
        }

        preferenceDao.saveAll(preferences);

        // 5. Generate the merit rank list.
        List<Allotment> ranklist =
                allotmentService.rankList();

        return "Demo dataset generated successfully. "
                + "College: " + savedCollege.getCollegeName()
                + ". Students: " + savedStudents.size()
                + ". Courses: " + courses.size()
                + ". Preferences: " + preferences.size()
                + ". Ranklist entries: " + ranklist.size()
                + ". College ID: " + savedCollege.getId();
    }

    private List<Course> createDemoCourses(College college) {

        List<Course> courses = new ArrayList<>();

        courses.add(createCourse(
                101,
                "Computer Science Engineering",
                100,
                college
        ));

        courses.add(createCourse(
                102,
                "Information Technology",
                80,
                college
        ));

        courses.add(createCourse(
                103,
                "Electrical and Electronics Engineering",
                80,
                college
        ));

        return courses;
    }

    private Course createCourse(
            int courseId,
            String courseName,
            int totalSeats,
            College college) {

        Course course = new Course();

        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setTotalSeats(totalSeats);
        course.setCollege(college);

        // Reuse the existing category-wise seat calculation.
        seatDistributionService.distributeSeats(
                course,
                CollegeType.PRIVATE
        );

        return course;
    }

    private List<String> generateCommunitySequence() {

        int[] remaining = COMMUNITY_COUNTS.clone();

        List<String> sequence =
                new ArrayList<>(TOTAL_STUDENTS);

        // Interleave communities in merit order.
        while (sequence.size() < TOTAL_STUDENTS) {

            for (int i = 0; i < COMMUNITIES.length; i++) {

                if (remaining[i] > 0) {

                    sequence.add(COMMUNITIES[i].name());

                    remaining[i]--;
                }
            }
        }

        return sequence;
    }
}
