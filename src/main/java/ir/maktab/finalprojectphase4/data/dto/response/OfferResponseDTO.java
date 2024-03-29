package ir.maktab.finalprojectphase4.data.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferResponseDTO {
    Long expertId;
    Long orderId;
    Long offerPrice;
    LocalDateTime proposedStartDate;
    int durationOfWork;
}
