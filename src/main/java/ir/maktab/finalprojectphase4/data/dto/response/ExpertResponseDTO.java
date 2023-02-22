package ir.maktab.finalprojectphase4.data.dto.response;

import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertResponseDTO {
    String firstname;
    String lastname;
    String emailAddress;
    ExpertStatus expertStatus;
}
