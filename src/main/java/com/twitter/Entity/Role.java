package com.twitter.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role", schema = "fsweb")
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String authority;

    @Override
    public boolean equals(Object obj) {

        if(obj == this)
            return true;

        if(obj == null || obj.getClass() != this.getClass())
            return false;

        Role role =  (Role) obj;

        return role.getId().equals(id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
