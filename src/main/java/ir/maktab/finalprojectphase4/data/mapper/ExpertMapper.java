package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.ChangePasswordDTO;
import ir.maktab.finalprojectphase4.data.dto.request.UserEmailDTO;
import ir.maktab.finalprojectphase4.data.dto.request.UserRegistrationDTO;
import ir.maktab.finalprojectphase4.data.dto.response.ExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.model.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpertMapper {
    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class);

    Expert responseDtoToModel(ExpertResponseDTO expertResponseDTO);

    Expert registerDtoToModel(UserRegistrationDTO expertRegistrationDTO);

    Expert emailDtoToModel(UserEmailDTO expertEmailDTO);

    Expert changePasswordDtoToModel(ChangePasswordDTO changePasswordDTO);

    ExpertResponseDTO modelToResponseDto(Expert expert);

    FilterExpertResponseDTO modelToFilterResponseDto(Expert expert);
}
