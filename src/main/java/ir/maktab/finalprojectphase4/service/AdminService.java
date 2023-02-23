package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.*;
import ir.maktab.finalprojectphase4.data.model.Admin;

import java.util.List;

public interface AdminService {
    void saveAccount(Admin admin);
    void addNewService(BaseServiceRequestDTO baseService);

    void removeService(Long baseServiceId);

    void addNewSubService(SubServiceRequestDTO subService);

    void removeSubService(Long subServiceId);

    void editSubService(UpdateSubServiceDTO subService);

    void confirmExpert(Long expertId);

    void changeExpertStatus(ChangeExpertStatusDTO changeExpertStatusDTO);

    void allocationServiceToExpert(ExpertSubServiceDTO expertSubServiceDTO);

    void removeExpertFromService(ExpertSubServiceDTO expertSubServiceDTO);

    List<BaseServiceResponseDTO> showAllService();

    List<SubServiceResponseDTO> showSubServices();

    List<SubServiceResponseDTO> showSubServicesByService(Long baseServiceRequestId);

    List<CustomerResponseDTO> showAllCustomer();

    List<ExpertResponseDTO> showAllExpert();

    List<ExpertResponseDTO> showAllNewExpert();

    List<ExpertResponseDTO> showAllConfirmedExpert();

    void checkExpertDelayForWork(Long offerId);

    void deActiveExpert(Long expertId);

    List<FilterExpertResponseDTO> expertFilter(FilterExpertDTO expertDTO);

    List<FilterCustomerResponseDTO> customerFilter(FilterCustomerDTO customerDTO);

    List<FilterOrderResponseDTO> ordersFilter(FilterOrderDTO filterOrderDTO);

    List<ExpertResponseDTO> showSubServicesExpert(Long subServiceId);

    int showNumberOfCustomersRegisteredOrders(Long customerId);

    int showNumberOfCustomersRegisteredOrdersByOrderStatus(CustomerOrdersStatusDTO customerOrdersStatusDTO);

    int showNumberOfExpertsPlacedOffers(Long expertId);

    int showNumberOfExpertsPlacedOffersByOfferStatus(ExpertOffersStatusDTO expertOffersStatusDTO);
}
