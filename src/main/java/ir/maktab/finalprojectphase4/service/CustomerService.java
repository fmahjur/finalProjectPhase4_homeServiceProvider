package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.*;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.model.Customer;

import java.util.List;

public interface CustomerService {
    void add(UserRegistrationDTO customerRegistrationDTO);

    void remove(UserEmailDTO customerEmailDto);

    void update(CustomerUpdateDTO customerUpdateDTO);

    Customer findByUsername(String customerUsername);

    Customer findByEmail(String customerEmail);

    List<CustomerResponseDTO> selectAll();

    List<CustomerResponseDTO> selectAllAvailableCustomer();

    void login(LoginDTO customerLogin);

    Customer changePassword(ChangePasswordDTO changePasswordDTO);

    void addNewOrder(Long customerId, SubmitOrderDTO SubmitOrderDTO);

    void deleteOrder(Long orderId);

    void editOrder(OrderUpdateDTO orderUpdateDTO);

    void choseAnExpertForOrder(Long offerId);

    void changeOrderStatusToStarted(Long orderId);

    void changeOrderStatusToDone(Long orderId);

    void addNewComment(CommentRequestDTO comment);

    List<OrderResponseDTO> showAllCustomerOrders(Long customerId);

    List<OrderResponseDTO> showAllCustomerOrdersByOrderStatus(Long customerId, OrderStatus orderStatus);

    OrderResponseDTO showOrderDetails(Long orderId);

    List<BaseServiceResponseDTO> showAllAvailableService();

    List<SubServiceResponseDTO> showSubServices(Long baseServiceId);

    Customer findById(Long id);

    List<OfferResponseDTO> showAllOfferForOrder(Long orderId);

    void updateCredit(Long customerId, Long newCredit);

    void payByCredit(Long orderId, Long customerId, Long expertId, Long amount);

    List<FilterCustomerResponseDTO> customersFilter(FilterCustomerDTO customerDTO);

    Long viewCredit(Long customerId);
}
