package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.enums.Role;
import ir.maktab.finalprojectphase4.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsernameAndRole(String username, Role role);
    boolean existsByEmailAndRole(String email, Role role);

    @Query("select c from Customer c where c.username= :username and c.role='ROLE_CUSTOMER'")
    Optional<Customer> findByUsername(String username);

    @Query("select c from Customer c where c.email= :email and c.role='ROLE_CUSTOMER'")
    Optional<Customer> findByEmail(String email);


    @Query("select c from Customer c where c.isDeleted= :isDeleted")
    List<Customer> findAllByDeletedIs(boolean isDeleted);

    @Modifying
    @Query("update Customer c set c.credit = :newCredit where c.id = :customerId")
    void updateCredit(Long customerId, Long newCredit);

    @Modifying
    @Query("update Customer set isDeleted=true where id = :customerId")
    void updateIsDeletedFlag(Long customerId);
}
