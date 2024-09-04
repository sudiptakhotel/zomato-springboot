package com.majorproject.zomato.ZomatoApp.entity;

import com.majorproject.zomato.ZomatoApp.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_user" ,
indexes = {
        @Index(name = "idx_user_email" , columnList = "email")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //set the authority for user
        //authority - role , permission

        return role.stream()
                .map(userRole -> {
                    return new SimpleGrantedAuthority("ROLE_"+userRole.name());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
}
