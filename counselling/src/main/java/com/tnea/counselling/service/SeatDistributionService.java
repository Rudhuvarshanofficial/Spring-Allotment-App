
package com.tnea.counselling.service;

import com.tnea.counselling.model.CollegeType;
import com.tnea.counselling.model.Course;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;

@Service
public class SeatDistributionService {

    private static final int TOTAL_PERCENTAGE_UNITS = 10_000;

    /*
     * Category order:
     * OC, BC, BCM, MBC, SC, SCA, ST
     */
    private static final int[] CATEGORY_PERCENTAGES = {
            3100,
            2650,
            350,
            2000,
            1500,
            300,
            100
    };

    public void distributeSeats(Course course, CollegeType collegeType) {

        int totalSeats = course.getTotalSeats();

        int counsellingSeats = calculateCounsellingSeats(
                totalSeats,
                collegeType
        );

        int[] categorySeats = calculateCategoryDistribution(
                counsellingSeats
        );

        course.setCounsellingSeats(counsellingSeats);

        // Preserve original category distribution
        course.setOcSeats(categorySeats[0]);
        course.setBcSeats(categorySeats[1]);
        course.setBcmSeats(categorySeats[2]);
        course.setMbcSeats(categorySeats[3]);
        course.setScSeats(categorySeats[4]);
        course.setScaSeats(categorySeats[5]);
        course.setStSeats(categorySeats[6]);

        // Initialize remaining seats from original distribution
        course.setRemainingOcSeats(categorySeats[0]);
        course.setRemainingBcSeats(categorySeats[1]);
        course.setRemainingBcmSeats(categorySeats[2]);
        course.setRemainingMbcSeats(categorySeats[3]);
        course.setRemainingScSeats(categorySeats[4]);
        course.setRemainingScaSeats(categorySeats[5]);
        course.setRemainingStSeats(categorySeats[6]);

        validateDistribution(course);
    }

    private int calculateCounsellingSeats(
            int totalSeats,
            CollegeType collegeType) {

        if (collegeType == null) {
            throw new IllegalArgumentException(
                    "College type must be specified"
            );
        }

        if (totalSeats < 1) {
            throw new IllegalArgumentException(
                    "Total seats must be at least 1"
            );
        }

        return switch (collegeType) {
            case GOVERNMENT -> totalSeats;
            case PRIVATE -> (totalSeats / 2) + (totalSeats % 2);
        };
    }

    private int[] calculateCategoryDistribution(int totalSeats) {

        int[] allocatedSeats = new int[CATEGORY_PERCENTAGES.length];
        long[] remainders = new long[CATEGORY_PERCENTAGES.length];

        int allocatedTotal = 0;

        for (int i = 0; i < CATEGORY_PERCENTAGES.length; i++) {

            long exactNumerator =
                    (long) totalSeats * CATEGORY_PERCENTAGES[i];

            allocatedSeats[i] = (int) (
                    exactNumerator / TOTAL_PERCENTAGE_UNITS
            );

            remainders[i] =
                    exactNumerator % TOTAL_PERCENTAGE_UNITS;

            allocatedTotal += allocatedSeats[i];
        }

        int remainingSeats = totalSeats - allocatedTotal;

        Integer[] categoryOrder = new Integer[
                CATEGORY_PERCENTAGES.length
                ];

        for (int i = 0; i < categoryOrder.length; i++) {
            categoryOrder[i] = i;
        }

        Arrays.sort(
                categoryOrder,
                Comparator
                        .<Integer>comparingLong(i -> remainders[i])
                        .reversed()
                        .thenComparingInt(i -> i)
        );

        for (int i = 0; i < remainingSeats; i++) {
            allocatedSeats[categoryOrder[i]]++;
        }

        int verifiedTotal = Arrays.stream(allocatedSeats).sum();

        if (verifiedTotal != totalSeats) {
            throw new IllegalStateException(
                    "Seat distribution total does not match counselling seats"
            );
        }

        return allocatedSeats;
    }

    private void validateDistribution(Course course) {

        long categoryTotal =
                (long) course.getOcSeats()
                        + course.getBcSeats()
                        + course.getBcmSeats()
                        + course.getMbcSeats()
                        + course.getScSeats()
                        + course.getScaSeats()
                        + course.getStSeats();

        if (categoryTotal != course.getCounsellingSeats()) {
            throw new IllegalStateException(
                    "Category seat total does not match counselling seats"
            );
        }

        long remainingTotal =
                (long) course.getRemainingOcSeats()
                        + course.getRemainingBcSeats()
                        + course.getRemainingBcmSeats()
                        + course.getRemainingMbcSeats()
                        + course.getRemainingScSeats()
                        + course.getRemainingScaSeats()
                        + course.getRemainingStSeats();

        if (remainingTotal != course.getCounsellingSeats()) {
            throw new IllegalStateException(
                    "Remaining seat total does not match counselling seats"
            );
        }
    }
}
