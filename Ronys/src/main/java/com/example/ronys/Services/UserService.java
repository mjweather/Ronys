package com.example.ronys.Services;

import com.example.ronys.Model.Users;
import com.example.ronys.MyExceptions.MyCustomDuplicateKeyException;
import com.example.ronys.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(@RequestBody Users user) throws MyCustomDuplicateKeyException {
        if (userRepository.existsByName(user.getName())) {
            throw new MyCustomDuplicateKeyException("Username already exists!");
        }
        userRepository.save(user);
    }
}
