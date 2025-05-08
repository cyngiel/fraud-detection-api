package com.frauddetection.api.repository.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_table")
@UserDefinition
public class User {

    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    @Username
    private String username;
    @Password
    private String password;
    @Roles
    private String role;


    @Builder
    public User(@NonNull String username, @NonNull String password, String role) {
        this.username = username;
        this.password = BcryptUtil.bcryptHash(password);
        this.role = role != null ? role : "USER";
    }
}
