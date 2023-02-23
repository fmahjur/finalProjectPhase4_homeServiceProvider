package ir.maktab.finalprojectphase4.data.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubServiceResponseDTO {
    String name;
    String description;
    Long basePrice;
}
