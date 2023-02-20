package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Offer extends BaseEntity {
    @OneToOne
    Expert expert;

    @ManyToOne
    Orders orders;

    @CreationTimestamp
    LocalDateTime offerDate;

    Double offerPrice;

    LocalDateTime proposedStartDate;

    int durationOfWork;

    LocalDateTime proposedEndDate;

    @Enumerated(EnumType.STRING)
    OfferStatus offerStatus;

    boolean isDeleted;

    public Offer() {
        this.offerStatus = OfferStatus.WAITING;
        this.isDeleted = false;
    }

    public Offer(Long id, Expert expert, Orders orders, Double offerPrice, LocalDateTime proposedStartDate, int durationOfWork, LocalDateTime proposedEndDate) {
        super(id);
        this.expert = expert;
        this.orders = orders;
        this.offerPrice = offerPrice;
        this.proposedStartDate = proposedStartDate;
        this.offerStatus = OfferStatus.WAITING;
        this.durationOfWork = durationOfWork;
        this.proposedEndDate = proposedEndDate;
    }
}
