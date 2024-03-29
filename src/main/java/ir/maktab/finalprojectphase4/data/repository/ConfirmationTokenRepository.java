package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Query("update ConfirmationToken t set t.createdAt= :localDateTime where t.token = :token")
    void updateConfirmedAt(String token, LocalDateTime localDateTime);
}
