package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import ir.maktab.finalprojectphase4.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    boolean existsByEmail(String email);


    boolean existsByUsername(String username);

    @Query("select e from Expert e where e.username= :username and e.role='ROLE_EXPERT'")
    Optional<Expert> findByUsername(String username);

    @Query("select e from Expert e where e.email= :email and e.role='ROLE_EXPERT'")
    Optional<Expert> findByEmail(String email);

    List<Expert> findAllByExpertStatus(ExpertStatus expertStatus);

    @Query("select e from Expert e where e.isDeleted= :isDeleted")
    List<Expert> findAllByDeletedIs(boolean isDeleted);

    @Modifying
    @Query("update Expert e set e.credit = :newCredit where e.id = :expertId")
    void updateCredit(Long expertId, Long newCredit);

    @Modifying
    @Query("update Expert e set e.isActive = :isActive where e.id = :expertId")
    void changeActivation(Long expertId, boolean isActive);

    @Modifying
    @Query("update Expert set isDeleted=true where id = :expertId")
    void updateIsDeletedFlag(Long expertId);
}
