package ir.maktab.finalprojectphase4.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterCustomerDTO {
    String firstname;
    String lastname;
    String email;
    String username;
    Boolean isActive;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    LocalDateTime CreationDate;

    Long credit;
    Long minCredit;
    Long maxCredit;
    String minCreationDate;
    String maxCreationDate;
}
