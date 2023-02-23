package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.SubmitOrderDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterOrderResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Orders responseDtoToModel(OrderResponseDTO orderResponseDTO);

    Orders submitDtoToModel(SubmitOrderDTO submitOrderDTO);

    OrderResponseDTO modelToResponseDto(Orders orders);

    FilterOrderResponseDTO modelToFilterResponseDto(Orders orders);

}
