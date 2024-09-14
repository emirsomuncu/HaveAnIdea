package com.emirsomuncu.HaveAnIdea.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Size(min = 1 , message = "You have to fill blank")
    @Column(name = "username")
    private String username;

    @Size(min = 1 , message = "You have to fill blank")
    @Column(name = "email" , unique = true , nullable = false)
    private String email;

    @Size(min = 6 , message = "Minimum password length is 6 characters")
    @Column(name = "password")
    private String password ;

    @Column(name = "role")
    private String role ;


    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE)
    private List<Post> posts ;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE)
    private List<Like> likes ;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
