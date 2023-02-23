package ir.maktab.finalprojectphase4.data.dto.response;

import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterExpertResponseDTO {
    String firstname;
    String lastname;
    String email;
    String username;
    ExpertStatus expertStatus;
    Double rate;
    Long credit;

    LocalDateTime creationDate;
}
