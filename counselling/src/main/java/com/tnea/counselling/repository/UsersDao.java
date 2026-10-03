package com.tnea.counselling.repository;

import com.tnea.counselling.model.Student;
import com.tnea.counselling.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<Users , Integer> {

    Users findByUsername(String username);
}
