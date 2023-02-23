package ir.maktab.finalprojectphase4.data.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmationToken extends BaseEntity {

    @Column(nullable = false)
    String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "account_id")
    Account account;

    @CreationTimestamp
    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime expireAt;

    LocalDateTime confirmAt;

    public ConfirmationToken(String token, Account account, LocalDateTime createdAt, LocalDateTime expireAt) {
        this.token = token;
        this.account = account;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }
}
