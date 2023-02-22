package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.model.BaseService;
import ir.maktab.finalprojectphase4.data.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long> {
    Optional<SubService> findByName(String subServiceName);

    boolean existsByName(String subServiceName);

    @Override
    boolean existsById(Long aLong);

    List<SubService> findAllByBaseService(BaseService baseService);

    @Query("select s from SubService s where s.isDeleted= :isDeleted")
    List<SubService> findAllByDeletedIs(boolean isDeleted);

    @Modifying
    @Query("update SubService set isDeleted=true where id = :subServiceId")
    void updateIsDeletedFlag(Long subServiceId);
}
