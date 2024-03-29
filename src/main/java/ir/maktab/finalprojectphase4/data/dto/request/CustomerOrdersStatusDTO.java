package ir.maktab.finalprojectphase4.data.dto.request;

import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerOrdersStatusDTO {
    Long customerId;
    OrderStatus orderStatus;
}
