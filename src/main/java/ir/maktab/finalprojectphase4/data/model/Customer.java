package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Customer extends Account {
    Long credit;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @Builder
    public Customer(String firstname, String lastname, String email, String username, String password, Boolean isActive) {
        super(firstname, lastname, email, username, password, isActive, Role.ROLE_CUSTOMER);
    }
}
