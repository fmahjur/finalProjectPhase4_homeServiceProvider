package ir.maktab.finalprojectphase4.data.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterCustomerResponseDTO {
    Long id;
    String firstname;
    String lastname;
    String email;
    String username;
    Long credit;
    LocalDateTime CreationDate;
}
