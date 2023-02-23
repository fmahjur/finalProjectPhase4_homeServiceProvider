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
    Orders order;

    @CreationTimestamp
    LocalDateTime offerDate;

    Long offerPrice;

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

    public Offer(Long id, Expert expert, Orders order, Long offerPrice, LocalDateTime proposedStartDate, int durationOfWork, LocalDateTime proposedEndDate) {
        super(id);
        this.expert = expert;
        this.order = order;
        this.offerPrice = offerPrice;
        this.proposedStartDate = proposedStartDate;
        this.offerStatus = OfferStatus.WAITING;
        this.durationOfWork = durationOfWork;
        this.proposedEndDate = proposedEndDate;
    }

    public Offer(Expert expert, Orders order, LocalDateTime offerDate, Long offerPrice, LocalDateTime proposedStartDate, int durationOfWork, LocalDateTime proposedEndDate, OfferStatus offerStatus) {
        this.expert = expert;
        this.order = order;
        this.offerDate = offerDate;
        this.offerPrice = offerPrice;
        this.proposedStartDate = proposedStartDate;
        this.durationOfWork = durationOfWork;
        this.proposedEndDate = proposedEndDate;
        this.offerStatus = offerStatus;
        this.isDeleted = false;
    }
}
