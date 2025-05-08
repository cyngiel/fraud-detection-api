package com.frauddetection.api.repository;

import com.frauddetection.api.repository.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public void deleteByUsername(String username) {
        User user = findByUsername(username);
        if (user != null) {
            delete(user);
        }
    }
}
