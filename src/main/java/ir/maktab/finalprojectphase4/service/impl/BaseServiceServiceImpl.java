package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.BaseServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.BaseServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.mapper.BaseServiceMapper;
import ir.maktab.finalprojectphase4.data.model.BaseService;
import ir.maktab.finalprojectphase4.data.repository.BaseServiceRepository;
import ir.maktab.finalprojectphase4.exception.DuplicateBaseServiceException;
import ir.maktab.finalprojectphase4.exception.NotFoundException;
import ir.maktab.finalprojectphase4.service.BaseServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BaseServiceServiceImpl implements BaseServiceService {
    private final BaseServiceRepository baseServiceRepository;

    @Override
    public void add(BaseServiceRequestDTO baseServiceRequestDTO) {
        if(baseServiceRepository.existsByName(baseServiceRequestDTO.getName()))
            throw new DuplicateBaseServiceException("This Service is already exist!");
        BaseService baseService = BaseServiceMapper.INSTANCE.requestDtoToModel(baseServiceRequestDTO);
        baseServiceRepository.save(baseService);
    }

    @Override
    public void remove(Long baseServiceId) {
        if(baseServiceRepository.findById(baseServiceId).isEmpty())
            throw new NotFoundException("This service does not exist!");
        baseServiceRepository.updateIsDeletedFlag(baseServiceId);
    }

    @Override
    public void update(BaseServiceRequestDTO baseServiceRequestDTO) {
        BaseService baseService = BaseServiceMapper.INSTANCE.requestDtoToModel(baseServiceRequestDTO);
        baseServiceRepository.save(baseService);
    }

    @Override
    public BaseServiceResponseDTO findByName(String baseServiceName) {
        BaseService baseService = baseServiceRepository.findByName(baseServiceName).orElseThrow(() -> new NotFoundException("not found"));
        return BaseServiceMapper.INSTANCE.modelToResponseDto(baseService);
    }

    @Override
    public BaseService findById(Long baseServiceId) {
        return baseServiceRepository.findById(baseServiceId).orElseThrow(() -> new NotFoundException("not found"));
    }

    @Override
    public boolean existById(Long baseServiceId) {
        return baseServiceRepository.existsById(baseServiceId);
    }

    @Override
    public List<BaseServiceResponseDTO> selectAll() {
        List<BaseService> baseServiceList = baseServiceRepository.findAll();
        List<BaseServiceResponseDTO> baseServiceResponseDTOList = new ArrayList<>();
        for (BaseService baseService : baseServiceList)
            baseServiceResponseDTOList.add(BaseServiceMapper.INSTANCE.modelToResponseDto(baseService));
        return baseServiceResponseDTOList;
    }

    @Override
    public List<BaseServiceResponseDTO> selectAllAvailableService() {
        List<BaseService> baseServiceList = baseServiceRepository.findAllByDeletedIs(false);
        List<BaseServiceResponseDTO> baseServiceResponseDTOList = new ArrayList<>();
        for (BaseService baseService : baseServiceList)
            baseServiceResponseDTOList.add(BaseServiceMapper.INSTANCE.modelToResponseDto(baseService));
        return baseServiceResponseDTOList;
    }
}
