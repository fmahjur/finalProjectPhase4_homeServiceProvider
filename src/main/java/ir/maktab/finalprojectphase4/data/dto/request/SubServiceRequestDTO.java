package ir.maktab.finalprojectphase4.data.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubServiceRequestDTO {
    Long baseServiceRequestID;
    String name;
    String description;
    Long basePrice;
}
