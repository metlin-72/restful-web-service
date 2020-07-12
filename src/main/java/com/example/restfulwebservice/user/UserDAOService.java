package com.example.restfulwebservice.user;


import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDAOService {
    private static List<User> users = new ArrayList<>();

    static{
        users.add(new User(1, "김원기", new Date(), "pass1", "730101-1111111"));
        users.add(new User(2, "임수일", new Date(), "pass2", "730102-2222222"));
        users.add(new User(3, "김진혁", new Date(), "pass3", "730103-3333333"));
        users.add(new User(4, "김세빈", new Date(), "pass4", "730105-5555555"));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if(user.getId() == 0){
            user.setId(users.size() + 1);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id){
        for (User user : users){
            if(user.getId() == id){
                return user;
            }
        }

        return null;
    }

    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public User updateUser(User user) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()){
            User orgUser = iterator.next();

            if(orgUser.getId() == user.getId()){
                orgUser.setJoinDate(now);
                orgUser.setName(user.getName());

                return orgUser;
            }
        }

        return null;
    }
}
