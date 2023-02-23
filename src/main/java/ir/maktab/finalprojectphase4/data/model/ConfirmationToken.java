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
public class Token extends BaseEntity {

    @Column(nullable = false)
    String token;

    @OneToOne(targetEntity = Expert.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    Expert account;

    @CreationTimestamp
    @Column(name = "creation_date")
    LocalDateTime createdAt;

    LocalDateTime expireAt;

    LocalDateTime confirmAt;

    public Token(Expert account) {
        this.account = account;
    }
}
