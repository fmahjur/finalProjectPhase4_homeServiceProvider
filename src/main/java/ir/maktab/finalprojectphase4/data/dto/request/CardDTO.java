package ir.maktab.finalprojectphase4.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardDTO {
    @Pattern(
            regexp = "(?<!\\d)\\d{16}(?!\\d)|(?<!\\d[ _-])(?<!\\d)\\d{4}(?=([_ -]))(?:\\1\\d{4}){3}(?![_ -]?\\d)",
            message = "the format of the card-number is incorrect!")
    String cardNumber;

    @Pattern(regexp = "^[0-9]{3,4}$", message = "the format of the cvv2 is incorrect!")
    String cvv2;

    String expireDate;
    String password;

    @Email
    String email;

}
