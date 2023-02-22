package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.BaseServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.BaseServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.model.BaseService;

import java.util.List;

public interface BaseServiceService {
    void add(BaseServiceRequestDTO baseServiceRequestDTO);

    void remove(Long baseServiceId);

    void update(BaseServiceRequestDTO baseServiceRequestDTO);

    BaseServiceResponseDTO findByName(String baseServiceName);

    BaseService findById(Long baseServiceId);

    boolean existById(Long baseServiceId);

    List<BaseServiceResponseDTO> selectAll();

    List<BaseServiceResponseDTO> selectAllAvailableService();
}
