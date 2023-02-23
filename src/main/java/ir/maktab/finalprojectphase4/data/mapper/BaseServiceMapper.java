package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.BaseServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.BaseServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.model.BaseService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BaseServiceMapper {
    BaseServiceMapper INSTANCE = Mappers.getMapper(BaseServiceMapper.class);

    BaseService responseDtoToModel(BaseServiceResponseDTO baseServiceResponseDTO);

    BaseService requestDtoToModel(BaseServiceRequestDTO baseServiceRequestDTO);

    BaseServiceResponseDTO modelToResponseDto(BaseService baseService);

    BaseServiceRequestDTO modelToRequestDto(BaseService baseService);
}
