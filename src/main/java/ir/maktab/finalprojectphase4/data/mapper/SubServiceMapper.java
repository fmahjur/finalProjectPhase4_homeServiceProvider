package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.SubServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.SubServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.model.SubService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubServiceMapper {
    SubServiceMapper INSTANCE = Mappers.getMapper(SubServiceMapper.class);

    SubService responseDtoToModel(SubServiceResponseDTO subServiceResponseDTO);

    SubService requestDtoToModel(SubServiceRequestDTO subServiceRequestDTO);

    SubServiceResponseDTO modelToResponseDto(SubService subService);

    SubServiceRequestDTO modelToRequestDto(SubService subService);
}
