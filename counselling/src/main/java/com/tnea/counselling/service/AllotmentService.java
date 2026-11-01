
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.Allotment;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllotmentService {

    @Autowired
    private AllotmentDao allotmentDao;

    @Autowired
    private StudentDao studentDao;

    @Transactional
    public List<Allotment> rankList() {

        List<Allotment> existingRankList =
                allotmentDao.findAllByOrderByMeritRankAsc();

        if (!existingRankList.isEmpty()) {
            return existingRankList;
        }

        List<Student> students =
                studentDao.findAllByOrderByCutoffDescIdAsc();

        if (students.isEmpty()) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "Empty student list",
                    "No student records"
            );
        }

        List<Allotment> allotments = new ArrayList<>();

        int meritRank = 1;

        for (Student student : students) {
            allotments.add(new Allotment(student, meritRank));
            meritRank++;
        }

        return allotmentDao.saveAll(allotments);
    }
}
