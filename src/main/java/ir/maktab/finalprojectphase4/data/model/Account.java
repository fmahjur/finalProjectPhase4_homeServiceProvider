package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account extends BaseEntity implements UserDetails {
    @Column(nullable = false)
    String firstname;
    @Column(nullable = false)
    String lastname;
    @Column(nullable = false)
    String email;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String password;

    @CreationTimestamp
    LocalDateTime registeryDate;
    Boolean isActive;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    Role role;

    boolean isDeleted;

    public Account(String firstname, String lastname, String email, String username, String password, Boolean isActive, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
        this.isDeleted = false;
    }

    @Override
    public String toString() {
        return "Account{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", role=" + role +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
