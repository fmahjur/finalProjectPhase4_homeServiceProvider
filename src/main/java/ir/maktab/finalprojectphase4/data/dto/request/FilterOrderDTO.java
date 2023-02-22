package ir.maktab.finalprojectphase4.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterOrderDTO {
    String address;
    String description;
    OrderStatus orderStatus;
    Long subServiceId;
    String subServiceName;
    String baseServiceName;

    Long minProposedPrice;
    Long proposedPrice;
    Long maxProposedPrice;

    String minCreationDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    LocalDateTime CreationDate;

    String maxCreationDate;
    Integer minDurationOfWork;
    Integer durationOfWork;
    Integer maxDurationOfWork;
}
