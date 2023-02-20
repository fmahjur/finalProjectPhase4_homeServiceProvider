package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders extends BaseEntity {
    String orderNumber;
    @ManyToOne(cascade = CascadeType.MERGE)
    Customer customer;

    @ManyToOne(cascade = CascadeType.MERGE)
    SubService subService;

    @OneToMany(mappedBy = "orders")
    List<Offer> offers = new ArrayList<>();

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Double CustomerProposedPrice;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @OneToOne
    Comment comment;

    boolean isDeleted;

    @CreationTimestamp
    LocalDateTime orderRegistrationDate;

    LocalDateTime workStartDate;

    int durationOfWork;

    LocalDateTime workEndDate;

    String address;

    public Orders() {
        this.orderStatus = OrderStatus.WAITING_FOR_EXPERTS_OFFER;
        this.isDeleted = false;
    }

    public Orders(Long id, String orderNumber, Customer customer, SubService subService, String description, Double customerProposedPrice, LocalDateTime workStartDate, int durationOfWork, String address) {
        super(id);
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.subService = subService;
        this.description = description;
        this.CustomerProposedPrice = customerProposedPrice;
        this.workStartDate = workStartDate;
        this.durationOfWork = durationOfWork;
        this.address = address;
        this.orderStatus = OrderStatus.WAITING_FOR_EXPERTS_OFFER;
        this.isDeleted = false;
    }

    public Orders(String orderNumber, Customer customer, SubService subService, String description, Double customerProposedPrice, LocalDateTime workStartDate, int durationOfWork, String address) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.subService = subService;
        this.description = description;
        this.CustomerProposedPrice = customerProposedPrice;
        this.workStartDate = workStartDate;
        this.durationOfWork = durationOfWork;
        this.address = address;
        this.orderStatus = OrderStatus.WAITING_FOR_EXPERTS_OFFER;
        this.isDeleted = false;
    }
}
