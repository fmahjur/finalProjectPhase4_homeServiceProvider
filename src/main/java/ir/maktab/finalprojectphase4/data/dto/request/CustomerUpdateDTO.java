package ir.maktab.finalprojectphase4.data.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateDTO {
    String firstname;
    String lastname;
    @Email
    String emailAddress;
    String username;
    Long credit;
}
