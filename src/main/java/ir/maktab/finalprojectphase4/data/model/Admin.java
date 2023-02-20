package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.Role;


public class Admin extends Account {
    public Admin() {
        super("admin", "admin", "admin@gmail.com", "admin", "Admin@123", true, Role.ROLE_ADMIN);
    }
}
