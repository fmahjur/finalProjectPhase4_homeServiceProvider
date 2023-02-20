package ir.maktab.finalprojectphase4.data.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertSubServiceDTO {
    Long subServiceId;
    Long expertId;
}
