package ir.maktab.finalprojectphase4.data.mapper;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.CustomerResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterCustomerResponseDTO;
import ir.maktab.finalprojectphase4.data.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer registerDtoToModel(UserRegistrationDTO customerRegistrationDTO);

    Customer emailDtoToModel(UserEmailDTO customerEmailDTO);

    Customer updateDtoToModel(CustomerUpdateDTO customerUpdateDTO);

    Customer changePasswordDtoToModel(ChangePasswordDTO changePasswordDTO);

    Customer filterDTOToModel(FilterCustomerDTO filterCustomerDTO);

    CustomerResponseDTO modelToResponseDto(Customer customer);

    FilterCustomerResponseDTO modelToFilterResponseDto(Customer customer);
}
