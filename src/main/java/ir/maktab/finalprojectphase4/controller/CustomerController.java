package ir.maktab.finalprojectphase4.controller;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.BaseServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.SubServiceResponseDTO;
import ir.maktab.finalprojectphase4.data.model.Customer;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.service.CaptchaService;
import ir.maktab.finalprojectphase4.service.impl.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;
    private CaptchaService captchaService;

    @PostMapping("/signup")
    @ResponseBody
    public void singUp(@Valid @RequestBody UserRegistrationDTO customerRegistrationDTO) {
        customerService.add(customerRegistrationDTO);
    }

    @PostMapping("/login")
    @ResponseBody
    public void login(@Valid @RequestBody LoginDTO customerLoginDto) {
        customerService.login(customerLoginDto);
    }

    @PutMapping("/change-password")
    @ResponseBody
    public void changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        customerService.changePassword(changePasswordDTO);
    }

    @GetMapping("/show-all-services")
    public List<BaseServiceResponseDTO> findAllBaseService() {
        return customerService.showAllAvailableService();
    }

    @GetMapping("/show-all-sub-services-by-service/{baseServiceId}")
    public List<SubServiceResponseDTO> findAllSubService(@PathVariable Long baseServiceId) {
        return customerService.showSubServices(baseServiceId);
    }

    @PostMapping("/submit-order")
    @ResponseBody
    public void submitOrder(@RequestBody SubmitOrderDTO submitOrderDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        customerService.addNewOrder(authenticatedCustomer.getId(), submitOrderDTO);
    }

    @PutMapping("/edit-order")
    @ResponseBody
    public void editOrder(@RequestBody OrderUpdateDTO orderUpdateDTO) {
        customerService.editOrder(orderUpdateDTO);
    }

    @PutMapping("/chose-an-expert-for-order/{offerId}")
    @ResponseBody
    public void choseExpertForOrder(@PathVariable Long offerId) {
        customerService.choseAnExpertForOrder(offerId);
    }

    @PutMapping("/order-start/{orderId}")
    @ResponseBody
    public void orderStarted(@PathVariable Long orderId) {
        customerService.changeOrderStatusToStarted(orderId);
    }

    @PutMapping("/order-done/{orderId}")
    @ResponseBody
    public void orderDone(@PathVariable Long orderId) {
        customerService.changeOrderStatusToDone(orderId);
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public void addCommentForExpertPerformance(@RequestBody CommentRequestDTO commentRequestDTO) {
        customerService.addNewComment(commentRequestDTO);
    }

    @GetMapping("/show-order-history")
    public List<OrderResponseDTO> showAllCustomersOrder(Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        return customerService.showAllCustomerOrders(authenticatedCustomer.getId());
    }

    @PostMapping("/show-order-history-by-order-status")
    public List<OrderResponseDTO> viewOrderHistory(@RequestBody OrderHistoryDTO orderHistoryDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        return customerService.showAllCustomerOrdersByOrderStatus(authenticatedCustomer.getId(), orderHistoryDTO.getOrderStatus());
    }

    @GetMapping("/show-order-details/{orderId}")
    public OrderResponseDTO showOrderDetails(@PathVariable Long orderId) {
        return customerService.showOrderDetails(orderId);
    }

    @GetMapping("/show-all-offer/{orderId}")
    public List<OfferResponseDTO> showAllOfferForOrder(@PathVariable Long orderId){
        return customerService.showAllOfferForOrder(orderId);
    }

    @PutMapping("/pay-by-credit")
    @ResponseBody
    public void payFromCredit(@RequestBody PayByCreditDTO payByCreditDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        customerService.payByCredit(authenticatedCustomer.getId(), payByCreditDTO);
    }

    @GetMapping("/payment")
    public String showPaymentPage(Model model){
        // create model object to store form data
        CardDTO card = new CardDTO();
        model.addAttribute("card", card);
        return "payment";
    }

    @PostMapping("/payment/pay")
    @ResponseBody
    private String pay(@Valid final CardDTO cardDto, final HttpServletRequest request) {
        final String response = request.getParameter("g-recaptcha-response");
        captchaService.processResponse(response);

        return "successful payment";
    }

}
