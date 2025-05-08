package com.frauddetection.api;


import com.frauddetection.api.repository.UserRepository;
import com.frauddetection.api.repository.entity.User;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StartupBeanForTesting {

    @Inject
    UserRepository userRepository;

    @Transactional
    public void init(@Observes StartupEvent ev) {
        userRepository.persist(User.builder()
                .username("testuser")
                .password("password")
                .build());
        userRepository.persist(User.builder()
                .username("admin")
                .password("admin")
                .role("ADMIN")
                .build());
    }
}
