package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.Role;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Entity
public class Admin extends Account {
    public Admin(String firstname, String lastname, String email, String username, String password, Role role) {
        super(firstname, lastname, email, username, password, role);
    }

    public Admin() {

    }
}
