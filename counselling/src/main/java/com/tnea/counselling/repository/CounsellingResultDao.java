
package com.tnea.counselling.repository;

import com.tnea.counselling.model.CounsellingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounsellingResultDao
        extends JpaRepository<CounsellingResult, Long> {

    List<CounsellingResult> findAllByRunIdOrderByMeritRankAsc(
            String runId
    );

    List<CounsellingResult> findAllByOrderByCreatedAtDesc();

    long countByRunId(String runId);
}
