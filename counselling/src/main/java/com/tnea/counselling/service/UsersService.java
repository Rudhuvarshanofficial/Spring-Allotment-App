
package com.tnea.counselling.service;

import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.UsersDao;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersDao usersDao;

    public UsersService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void register(Users user) {
        usersDao.save(user);
    }
}
