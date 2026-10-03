package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.RegistrationException;
import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
@Service
public class UsersService {



    @Autowired
    private UsersDao usersDao;

    public void register(Users user){
        try {
            usersDao.save(user);
        }catch (Exception e){
            throw new RegistrationException(LocalTime.now(),e.getMessage(),"Error while inserting user");
        }
    }

}
