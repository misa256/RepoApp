package com.example.springbootreporestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    emailを一意とし、認証・認可もemailで行う
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "roles", nullable = false)
    private String[] roles;
   @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    private Set<Report> getReports() {
        return reports;
    }

    public  Set<Report> acquireReports(){
        return getReports();
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }


}
