package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.SubServiceRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.request.UpdateSubServiceDTO;
import ir.maktab.finalprojectphase4.data.dto.response.SubServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.model.SubService;

import java.util.List;

public interface SubServiceService {
    void add(SubServiceRequestDTO subServiceRequestDTO);

    void remove(Long subServiceRequestId) ;

    void update(UpdateSubServiceDTO updateSubServiceDTO);

    SubService findByName(String subServiceName);
    SubService findById(Long subServiceId);

    List<SubServiceResponseDTO> selectAll();

    List<SubServiceResponseDTO> selectAllAvailableService();

    List<SubServiceResponseDTO> getSubServicesByService(Long baseServiceRequestId);
}
