package ir.maktab.finalprojectphase4.data.dto.request;

import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterOrderDTO {
    String address;
    String description;
    OrderStatus orderStatus;
    Long subServiceId;
    String subServiceName;
    String baseServiceName;

    Long minProposedPrice;
    Long proposedPrice;
    Long maxProposedPrice;
    String minCreationDate;
    LocalDateTime CreationDate;
    String maxCreationDate;
    int minDurationOfWork;
    int durationOfWork;
    int masDurationOfWork;
}
