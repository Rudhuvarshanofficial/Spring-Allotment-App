package com.tnea.counselling.service;

import com.tnea.counselling.configuration.UsersPrinciple;
import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = dao.findByUsername(username);
        if(user == null){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found");
        }
        else return new UsersPrinciple(user);
    }
}
