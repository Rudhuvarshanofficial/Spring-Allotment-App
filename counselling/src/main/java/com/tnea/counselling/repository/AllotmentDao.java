
package com.tnea.counselling.repository;

import com.tnea.counselling.model.Allotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllotmentDao extends JpaRepository<Allotment, Integer> {

    Allotment findByStudentId(int studentId);

    boolean existsByStudentId(int studentId);

    List<Allotment> findAllByOrderByMeritRankAsc();

    List<Allotment> findByisAllotedFalse();
}
