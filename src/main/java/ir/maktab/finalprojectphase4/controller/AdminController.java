package ir.maktab.finalprojectphase4.controller;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.*;
import ir.maktab.finalprojectphase4.data.enums.Role;
import ir.maktab.finalprojectphase4.data.model.Admin;
import ir.maktab.finalprojectphase4.service.AdminService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

/*    @PostConstruct
    private void test(){
        String password = bCryptPasswordEncoder.encode("Admin@123");
        Admin admin = new Admin("customer1",
                "customer1",
                "reihaneh763@gmail.com",
                "customer1",
                password,
                Role.ROLE_EXPERT);

        adminService.saveAccount(admin);
    }*/

    @PostMapping("/add-base-service")
    public void addMainService(@RequestBody BaseServiceRequestDTO baseServiceRequestDTO) {
        adminService.addNewService(baseServiceRequestDTO);
    }

    @DeleteMapping("/delete-base-service/{baseServiceId}")
    public void deleteMainService(@PathVariable Long baseServiceId) {
        adminService.removeService(baseServiceId);
    }

    @PostMapping("/add-sub-service")
    public void addSubService(@RequestBody SubServiceRequestDTO subServiceRequestDTO) {
        adminService.addNewSubService(subServiceRequestDTO);
    }

    @DeleteMapping("/delete-sub-service/{subServiceId}")
    public void deleteSubService(@PathVariable Long subServiceId) {
        adminService.removeSubService(subServiceId);
    }

    @PostMapping("/allocation-service-to-expert")
    public void allocationServiceToExpert(@RequestBody ExpertSubServiceDTO expertSubServiceDTO) {
        adminService.allocationServiceToExpert(expertSubServiceDTO);
    }

    @DeleteMapping("/remove-service-from-expert")
    public void deleteServiceFromExpert(@RequestBody ExpertSubServiceDTO expertSubServiceDTO) {
        adminService.removeExpertFromService(expertSubServiceDTO);
    }

    @GetMapping("/show-all-services")
    public List<BaseServiceResponseDTO> findAllBaseService() {
        return adminService.showAllService();
    }

    @GetMapping("/show-all-sub-services")
    public List<SubServiceResponseDTO> findAllSubService() {
        return adminService.showSubServices();
    }

    @GetMapping("/show-all-sub-services-by-service/{baseServiceId}")
    public List<SubServiceResponseDTO> findAllSubServiceByService(@PathVariable Long baseServiceId) {
        return adminService.showSubServicesByService(baseServiceId);
    }

    @PutMapping("/edit-sub-service")
    public void editSubServiceBasePriceAndDescription(@RequestBody UpdateSubServiceDTO updateSubServiceDTO) {
        adminService.editSubService(updateSubServiceDTO);
    }

    @GetMapping("/show-all-expert")
    public List<ExpertResponseDTO> findAllExpert() {
        return adminService.showAllExpert();
    }

    @PutMapping("/confirm-expert/{expertId}")
    public void confirmExpert(@PathVariable Long expertId) {
        adminService.confirmExpert(expertId);
    }

    @PutMapping("/change-expert-status")
    public void changeExpertStatus(@RequestBody ChangeExpertStatusDTO changeExpertStatusDTO) {
        adminService.changeExpertStatus(changeExpertStatusDTO);
    }

    @PutMapping("/check-expert-delay/{offerId}")
    public void checkExpertDelayForDoingWork(@PathVariable Long offerId) {
        adminService.checkExpertDelayForWork(offerId);
    }

    @PutMapping("/change-expert-activation/{expertId}")
    public void inactiveExpertAccount(@PathVariable Long expertId) {
        adminService.deActiveExpert(expertId);
    }

    @PostMapping("/filter-experts")
    public List<FilterExpertResponseDTO> expertsFilter(@RequestBody FilterExpertDTO expertDTO) {
        return adminService.expertFilter(expertDTO);
    }

    @PostMapping("/filter-customers")
    public List<FilterCustomerResponseDTO> customersFilter(@RequestBody FilterCustomerDTO customerDTO) {
        return adminService.customerFilter(customerDTO);
    }

    @PostMapping("/filter-orders")
    public List<FilterOrderResponseDTO> customersFilter(@RequestBody FilterOrderDTO filterOrderDTO) {
        return adminService.ordersFilter(filterOrderDTO);
    }

    @GetMapping("/show-sub-services-expert/{subServiceId}")
    public List<ExpertResponseDTO> viewSubServiceExperts(@PathVariable Long subServiceId) {
        return adminService.showSubServicesExpert(subServiceId);
    }

    @GetMapping("/show-number-of-customers-registered-orders/{customerId}")
    public int viewNumberOfCustomersRegisteredOrders(@PathVariable Long customerId) {
        return adminService.showNumberOfCustomersRegisteredOrders(customerId);
    }

    @GetMapping("/show-number-of-customers-registered-orders-by-order-status")
    public int viewNumberOfCustomersRegisteredOrdersByOrderStatus(@RequestBody CustomerOrdersStatusDTO customerOrdersStatusDTO) {
        return adminService.showNumberOfCustomersRegisteredOrdersByOrderStatus(customerOrdersStatusDTO);
    }

    @GetMapping("/show-number-of-experts-placed-offers/{expertId}")
    public int viewNumberOfExpertsPlacedOffers(@PathVariable Long expertId) {
        return adminService.showNumberOfExpertsPlacedOffers(expertId);
    }

    @GetMapping("/show-number-of-experts-placed-offers-by-offer-status")
    public int viewNumberOfExpertsPlacedOffersByOfferStatus(@RequestBody ExpertOffersStatusDTO expertOffersStatusDTO) {
        return adminService.showNumberOfExpertsPlacedOffersByOfferStatus(expertOffersStatusDTO);
    }
}
