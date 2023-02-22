package ir.maktab.finalprojectphase4.data.dto.request;

import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertOffersStatusDTO {
    Long expertId;
    OfferStatus offerStatus;
}
