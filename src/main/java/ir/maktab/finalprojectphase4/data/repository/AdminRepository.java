package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.model.Account;
import ir.maktab.finalprojectphase4.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
