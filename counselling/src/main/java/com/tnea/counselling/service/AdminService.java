
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.CounsellingException;
import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.*;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class AdminService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private AllotmentDao allotmentDao;

    @Autowired
    private CourseDao courseDao;

    public List<Student> getAllStudents() {

        List<Student> students = studentDao.findAll();

        if (students.isEmpty()) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "Not registered yet",
                    "Empty student table"
            );
        }

        return students;
    }

    @Transactional
    public String runCounselling() {

        List<Allotment> ranklist =
                allotmentDao.findAllByOrderByMeritRankAsc();

        if (ranklist.isEmpty()) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "No details found",
                    "No records available in allotment"
            );
        }

        // Prevent repeated execution, including when nobody was allotted.
        boolean alreadyProcessed = ranklist.stream()
                .anyMatch(Allotment::isCounsellingProcessed);

        if (alreadyProcessed) {
            throw new CounsellingException(
                    "Counselling has already been completed. "
                            + "Create a fresh counselling dataset to run again.",
                    CounsellingException.ErrorType.CONFLICT
            );
        }

        for (Allotment allotment : ranklist) {

            Student student = studentDao.findById(
                    allotment.getStudentId()
            ).orElseThrow(() -> new NoRecordException(
                    LocalTime.now(),
                    "Student not found",
                    "Student ID: " + allotment.getStudentId()
            ));

            Community community = normalizeCommunity(
                    allotment.getStudentCommunity()
            );

            List<Preference> preferences = student.getPreferences();

            if (preferences != null && !preferences.isEmpty()) {

                preferences.sort(
                        Comparator.comparingInt(Preference::getPriority)
                );

                for (Preference preference : preferences) {

                    Course course = courseDao.findByCourseAndCollegeId(
                            preference.getCourseid(),
                            preference.getCollegeid()
                    );

                    if (course == null) {
                        continue;
                    }

                    // Every student gets OC priority.
                    if (tryAllocateSeat(course, Community.OC)) {

                        completeAllotment(
                                allotment,
                                preference,
                                course,
                                Community.OC.name()
                        );

                        break;
                    }

                    // OC-community students have no separate
                    // community quota. They can only get OC seats.
                    if (community == Community.OC) {
                        continue;
                    }

                    // If OC is unavailable, check the student's
                    // own community quota.
                    if (tryAllocateSeat(course, community)) {

                        completeAllotment(
                                allotment,
                                preference,
                                course,
                                community.name()
                        );

                        break;
                    }
                }
            }

            // Mark every student as processed, even if not allotted.
            allotment.setCounsellingProcessed(true);
        }

        allotmentDao.saveAll(ranklist);

        return "Counselling completed successfully";
    }

    private Community normalizeCommunity(String community) {

        if (community == null || community.isBlank()) {
            throw new CounsellingException(
                    "Student community cannot be empty.",
                    CounsellingException.ErrorType.INVALID_DATA
            );
        }

        String normalized = community.trim().toUpperCase(Locale.ROOT);

        try {
            return Community.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw new CounsellingException(
                    "Unsupported student community: " + community,
                    CounsellingException.ErrorType.INVALID_DATA
            );
        }
    }

    private boolean tryAllocateSeat(
            Course course,
            Community category) {

        switch (category) {

            case OC:
                if (course.getRemainingOcSeats() > 0) {
                    course.setRemainingOcSeats(
                            course.getRemainingOcSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case BC:
                if (course.getRemainingBcSeats() > 0) {
                    course.setRemainingBcSeats(
                            course.getRemainingBcSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case BCM:
                if (course.getRemainingBcmSeats() > 0) {
                    course.setRemainingBcmSeats(
                            course.getRemainingBcmSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case MBC:
                if (course.getRemainingMbcSeats() > 0) {
                    course.setRemainingMbcSeats(
                            course.getRemainingMbcSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case SC:
                if (course.getRemainingScSeats() > 0) {
                    course.setRemainingScSeats(
                            course.getRemainingScSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case SCA:
                if (course.getRemainingScaSeats() > 0) {
                    course.setRemainingScaSeats(
                            course.getRemainingScaSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;

            case ST:
                if (course.getRemainingStSeats() > 0) {
                    course.setRemainingStSeats(
                            course.getRemainingStSeats() - 1
                    );
                    courseDao.save(course);
                    return true;
                }
                break;
        }

        return false;
    }

    private void completeAllotment(
            Allotment allotment,
            Preference preference,
            Course course,
            String allocationCategory) {

        allotment.setAlloted(true);

        allotment.setAllotedCollege(
                course.getCollege() != null
                        ? course.getCollege().getCollegeName()
                        : preference.getCollege().getCollegeName()
        );

        allotment.setAllotedCourse(course.getCourseName());

        allotment.setAllottedCollegeId(preference.getCollegeid());

        allotment.setAllottedCourseId(preference.getCourseid());

        allotment.setAllottedPreferencePriority(
                preference.getPriority()
        );

        allotment.setAllocationCategory(allocationCategory);
    }
}
