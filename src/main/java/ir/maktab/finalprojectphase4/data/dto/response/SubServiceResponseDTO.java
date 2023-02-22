package ir.maktab.finalprojectphase4.data.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubServiceResponseDTO {
    BaseServiceResponseDTO baseServiceResponseDTO;
    String name;
    String description;
    Double basePrice;
}
