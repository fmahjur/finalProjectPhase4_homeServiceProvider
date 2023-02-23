package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.OfferRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.model.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Offer responseDtoToModel(OfferResponseDTO baseServiceResponseDTO);

    Offer requestDtoToModel(OfferRequestDTO baseServiceRequestDTO);

    OfferResponseDTO modelToResponseDto(Offer offer);

    OfferRequestDTO modelToRequestDto(Offer offer);
}
