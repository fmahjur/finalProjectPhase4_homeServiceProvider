package ir.maktab.finalprojectphase4.data.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSubServiceDTO {
    Long baseServiceRequestID;
    Long subServiceID;
    String name;
    String description;
    Long basePrice;
}
