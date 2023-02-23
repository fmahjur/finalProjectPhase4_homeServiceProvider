package ir.maktab.finalprojectphase4.data.dto.response;

import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.model.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponseDTO {
    List<Offer> offers = new ArrayList<>();
    String description;
    Long CustomerProposedPrice;
    OrderStatus orderStatus;
    Comment comment;
    Date orderRegistrationDate;
    Date workStartDate;
    int durationOfWork;
    String address;
}
