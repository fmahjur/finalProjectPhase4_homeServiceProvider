package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.SubServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.request.UpdateSubServiceDTO;
import ir.maktab.finalprojectphase4.data.dto.response.SubServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.mapper.SubServiceMapper;
import ir.maktab.finalprojectphase4.data.model.BaseService;
import ir.maktab.finalprojectphase4.data.model.SubService;
import ir.maktab.finalprojectphase4.data.repository.SubServiceRepository;
import ir.maktab.finalprojectphase4.exception.DuplicateSubServiceException;
import ir.maktab.finalprojectphase4.exception.NotFoundException;
import ir.maktab.finalprojectphase4.service.SubServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceRepository subServiceRepository;
    private final BaseServiceServiceImpl baseServiceService;

    @Override
    public void add(SubServiceRequestDTO subServiceRequestDTO) {
        if (!baseServiceService.existById(subServiceRequestDTO.getBaseServiceRequestID()))
            throw new NotFoundException("This service does not exist!");
        if (subServiceRepository.existsByName(subServiceRequestDTO.getName()))
            throw new DuplicateSubServiceException("This sub service is already exist!");
        SubService subService = SubServiceMapper.INSTANCE.requestDtoToModel(subServiceRequestDTO);
        subService.setBaseService(getBaseServiceObject(subServiceRequestDTO.getBaseServiceRequestID()));
        subServiceRepository.save(subService);
    }

    @Override
    public void remove(Long subServiceRequestId) {
        subServiceRepository.updateIsDeletedFlag(subServiceRequestId);
    }

    @Override
    public void update(UpdateSubServiceDTO updateSubServiceDTO) {
        SubService subService = subServiceRepository.findById(updateSubServiceDTO.getSubServiceID())
                .orElseThrow(() -> new NotFoundException("This sub service does not exist!"));
        if (!Objects.equals(String.valueOf(updateSubServiceDTO.getBaseServiceRequestID()), ""))
            subService.setBaseService(getBaseServiceObject(updateSubServiceDTO.getBaseServiceRequestID()));
        if (!Objects.equals(String.valueOf(updateSubServiceDTO.getName()), ""))
            subService.setName(updateSubServiceDTO.getName());
        if (!Objects.equals(String.valueOf(updateSubServiceDTO.getDescription()), ""))
            subService.setDescription(updateSubServiceDTO.getDescription());
        if (!Objects.equals(updateSubServiceDTO.getBasePrice(), null))
            subService.setBasePrice(updateSubServiceDTO.getBasePrice());
        subServiceRepository.save(subService);
    }

    @Override
    public SubService findByName(String subServiceName) {
        return subServiceRepository.findByName(subServiceName).orElseThrow(() -> new NotFoundException("This sub service does not exist!"));
    }

    @Override
    public SubService findById(Long subServiceId) {
        return subServiceRepository.findById(subServiceId).orElseThrow(() -> new NotFoundException("This sub service does not exist!"));
    }

    @Override
    public List<SubServiceResponseDTO> selectAll() {
        List<SubService> subServiceList = subServiceRepository.findAll();
        List<SubServiceResponseDTO> subServiceResponseDTOList = new ArrayList<>();
        for (SubService subService : subServiceList)
            subServiceResponseDTOList.add(SubServiceMapper.INSTANCE.modelToResponseDto(subService));
        return subServiceResponseDTOList;
    }

    @Override
    public List<SubServiceResponseDTO> selectAllAvailableService() {
        List<SubService> subServiceList = subServiceRepository.findAllByDeletedIs(false);
        List<SubServiceResponseDTO> subServiceResponseDTOList = new ArrayList<>();
        for (SubService subService : subServiceList)
            subServiceResponseDTOList.add(SubServiceMapper.INSTANCE.modelToResponseDto(subService));
        return subServiceResponseDTOList;
    }

    @Override
    public List<SubServiceResponseDTO> getSubServicesByService(Long baseServiceRequestId) {
        BaseService baseService = baseServiceService.findById(baseServiceRequestId);
        List<SubService> subServiceList = subServiceRepository.findAllByBaseService(baseService);
        List<SubServiceResponseDTO> subServiceResponseDTOList = new ArrayList<>();
        for (SubService subService : subServiceList)
            subServiceResponseDTOList.add(SubServiceMapper.INSTANCE.modelToResponseDto(subService));
        return subServiceResponseDTOList;
    }

    private BaseService getBaseServiceObject(Long id) {
        BaseService baseService = new BaseService();
        baseService.setId(id);
        return baseService;
    }
}
