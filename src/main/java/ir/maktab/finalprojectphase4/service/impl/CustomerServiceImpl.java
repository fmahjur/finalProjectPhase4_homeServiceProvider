package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.*;
import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.enums.Role;
import ir.maktab.finalprojectphase4.data.mapper.CustomerMapper;
import ir.maktab.finalprojectphase4.data.mapper.OrderMapper;
import ir.maktab.finalprojectphase4.data.model.*;
import ir.maktab.finalprojectphase4.data.repository.CustomerRepository;
import ir.maktab.finalprojectphase4.exception.*;
import ir.maktab.finalprojectphase4.service.CustomerService;
import ir.maktab.finalprojectphase4.service.EmailSenderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenServiceImpl tokenService;
    private final EmailSenderService emailSenderService;
    private final OrderServiceImpl orderService;
    private final OfferServiceImpl offerService;
    private final ExpertServiceImpl expertService;
    private final BaseServiceServiceImpl baseServiceService;
    private final SubServiceServiceImpl subServiceService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String signUp(Customer customer) {
        if (customerRepository.existsByEmailAndRole(customer.getEmail(), Role.ROLE_CUSTOMER))
            throw new DuplicateEmailException("This Email is already Exist!");
        if (customerRepository.existsByUsernameAndRole(customer.getUsername(), Role.ROLE_CUSTOMER))
            throw new DuplicateEmailException("This username is already Exist!");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_CUSTOMER);
        customerRepository.save(customer);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, customer, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
        confirmationToken.setToken(UUID.randomUUID().toString());
        tokenService.add(confirmationToken);

        SimpleMailMessage mailMessage = emailSenderService.createEmail(customer.getEmail(), confirmationToken.getToken(), "customer");
        emailSenderService.sendEmail(mailMessage);

        return token;
    }

    @Override
    public void remove(UserEmailDTO customerEmailDto) {
        Customer customer = findByUsername(customerEmailDto.getEmail());
        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    @Override
    public void update(CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = CustomerMapper.INSTANCE.updateDtoToModel(customerUpdateDTO);
        customerRepository.save(customer);
    }

    @Override
    public Customer findByUsername(String customerUsername) {
        return customerRepository.findByUsername(customerUsername).orElseThrow(() -> new NotFoundException("This username was not found"));
    }

    @Override
    public Customer findByEmail(String customerEmail) {
        return customerRepository.findByEmail(customerEmail).orElseThrow(() -> new NotFoundException("This email was not found"));
    }

    @Override
    public List<CustomerResponseDTO> selectAll() {
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerResponseDTO> customerResponseDTOList = new ArrayList<>();
        for (Customer customer : customerList)
            customerResponseDTOList.add(CustomerMapper.INSTANCE.modelToResponseDto(customer));
        return customerResponseDTOList;
    }

    @Override
    public List<CustomerResponseDTO> selectAllAvailableCustomer() {
        List<Customer> availableCustomerList = customerRepository.findAllByDeletedIs(false);
        List<CustomerResponseDTO> customerResponseDTOList = new ArrayList<>();
        for (Customer customer : availableCustomerList)
            customerResponseDTOList.add(CustomerMapper.INSTANCE.modelToResponseDto(customer));
        return customerResponseDTOList;
    }
    @Override
    public Customer changePassword(ChangePasswordDTO changePasswordDTO) {
        if (changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword()))
            throw new ChangePasswordException("oldPassword and newPassword can not be same!");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new ChangePasswordException("newPassword and confirmNewPassword must be same!");
        Customer customer = findByUsername(changePasswordDTO.getUsername());
        customer.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public void addNewOrder(Long customerId, SubmitOrderDTO submitOrderDTO) {
        Customer customer = findById(customerId);
        SubService subService = subServiceService.findById(submitOrderDTO.getSubServiceId());
        Orders order = new Orders(customer,
                subService,
                submitOrderDTO.getDescription(),
                submitOrderDTO.getCustomerProposedPrice(),
                submitOrderDTO.getWorkStartDate(),
                submitOrderDTO.getDurationOfWork(),
                submitOrderDTO.getAddress());
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERTS_OFFER);
        orderService.add(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderService.remove(orderId);
    }

    @Override
    public void editOrder(OrderUpdateDTO orderUpdateDTO) {
        Orders order = orderService.findById(orderUpdateDTO.getOrderId());
        order.setDescription(orderUpdateDTO.getDescription());
        order.setCustomerProposedPrice(orderUpdateDTO.getCustomerProposedPrice());
        order.setWorkStartDate(orderUpdateDTO.getWorkStartDate());
        order.setDurationOfWork(orderUpdateDTO.getDurationOfWork());
        order.setAddress(orderUpdateDTO.getAddress());
        orderService.update(order);
    }

    @Override
    public void choseAnExpertForOrder(Long offerId) {
        Offer offer = offerService.findById(offerId);
        if (offer.getOfferStatus().equals(OfferStatus.ACCEPTED))
            throw new OfferAcceptedException("this offer accepted!");
        Orders order = offer.getOrder();
        for (Offer offer1 : order.getOffers()) {
            if (Objects.equals(offer1, offer)) {
                offer1.setOfferStatus(OfferStatus.ACCEPTED);
                order.setWorkStartDate(offer1.getProposedStartDate());
            } else
                offer1.setOfferStatus(OfferStatus.REJECTED);
            offerService.update(offer1);
        }
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_TO_COME);
        orderService.update(order);
    }

    @Override
    public void changeOrderStatusToStarted(Long orderId) {
        Orders order = orderService.findById(orderId);
        if (order.getWorkStartDate().isBefore(ChronoLocalDateTime.from(LocalDate.now())))
            throw new WorkStartDateException("work can not start before now!");
        order.setOrderStatus(OrderStatus.STARTED);
        orderService.update(order);
    }

    @Override
    public void changeOrderStatusToDone(Long orderId) {
        Orders orders = orderService.findById(orderId);
        orders.setOrderStatus(OrderStatus.DONE);
        orderService.update(orders);
    }

    @Override
    public void addNewComment(CommentRequestDTO comment) {
        if (comment.getScore() == null)
            throw new NullScoreException("The score cannot be null!");
        if (comment.getScore() < 0 || comment.getScore() > 6)
            throw new ScoreOutsideDefinedRangeException("Expert score should be between 1 and 5!");
        expertService.receivedNewComment(comment);
    }

    @Override
    public List<OrderResponseDTO> showAllCustomerOrders(Long customerId) {
        Customer customer = findById(customerId);
        if (customer.getOrders() == null)
            throw new NotFoundException("You have not placed an order yet!");
        return orderService.selectAllCustomersOrders(customer);
    }

    @Override
    public List<OrderResponseDTO> showAllCustomerOrdersByOrderStatus(Long customerId, OrderStatus orderStatus) {
        Customer customer = findById(customerId);
        if (customer.getOrders() == null)
            throw new NotFoundException("You have not placed an order yet!");
        return orderService.selectAllCustomersOrdersByOrderStatus(customer, orderStatus);
    }

    @Override
    public OrderResponseDTO showOrderDetails(Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @Override
    public List<BaseServiceResponseDTO> showAllAvailableService() {
        return baseServiceService.selectAll();
    }

    @Override
    public List<SubServiceResponseDTO> showSubServices(Long baseServiceId) {
        return subServiceService.getSubServicesByService(baseServiceId);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("This customer was not found!"));
    }

    @Override
    public List<OfferResponseDTO> showAllOfferForOrder(Long orderId) {
        return offerService.selectAllByOrder(orderId);
    }

    @Override
    public void updateCredit(Long customerId, Long newCredit) {
        customerRepository.updateCredit(customerId, newCredit);
    }

    @Override
    public void payByCredit(Long customerId, PayByCreditDTO payByCreditDTO) {
        Orders order = orderService.findById(payByCreditDTO.getOrderId());
        if (!order.getOrderStatus().equals(OrderStatus.DONE))
            throw new OrderStatusException("the status of this order is not yet \"DONE\"!");
        Customer customer = findById(customerId);
        if (customer.getCredit() < payByCreditDTO.getAmount())
            throw new InsufficientFoundsException("Insufficient founds!");
        Expert expert = expertService.findById(payByCreditDTO.getExpertId());
        orderService.changeOrderStatus(payByCreditDTO.getOrderId(), OrderStatus.PAID);
        updateCredit(customerId, customer.getCredit() - payByCreditDTO.getAmount());
        expertService.updateCredit(payByCreditDTO.getExpertId(),
                expert.getCredit() + (long) (payByCreditDTO.getAmount() * 0.7));
    }

    @Override
    public List<FilterCustomerResponseDTO> customersFilter(FilterCustomerDTO customerDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerCriteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = customerCriteriaQuery.from(Customer.class);
        createFilters(customerDTO, predicateList, criteriaBuilder, customerRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        customerCriteriaQuery.select(customerRoot).where(predicates);
        List<Customer> resultList = entityManager.createQuery(customerCriteriaQuery).getResultList();
        List<FilterCustomerResponseDTO> filterCustomerResponseDTOList = new ArrayList<>();
        for (Customer customer : resultList)
            filterCustomerResponseDTOList.add(CustomerMapper.INSTANCE.modelToFilterResponseDto(customer));
        return filterCustomerResponseDTOList;
    }

    private void createFilters(FilterCustomerDTO customerDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Customer> customerRoot) {
        if (customerDTO.getFirstname() != null) {
            String firstname = "%" + customerDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("firstname"), firstname));
        }
        if (customerDTO.getLastname() != null) {
            String lastname = "%" + customerDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("lastname"), lastname));
        }
        if (customerDTO.getEmail() != null) {
            String email = "%" + customerDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("email"), email));
        }
        if (customerDTO.getUsername() != null) {
            String username = "%" + customerDTO.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("username"), username));
        }
        if (customerDTO.getEnabled() != null) {
            predicateList.add(criteriaBuilder.equal(customerRoot.get("enabled"), customerDTO.getEnabled()));
        }

        if (customerDTO.getMinCredit() == null && customerDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.lt(customerRoot.get("credit"), customerDTO.getMaxCredit()));
        }
        if (customerDTO.getMinCredit() != null && customerDTO.getMaxCredit() == null) {
            predicateList.add(criteriaBuilder.gt(customerRoot.get("credit"), customerDTO.getMinCredit()));
        }
        if (customerDTO.getMinCredit() != null && customerDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.between(customerRoot.get("credit"), customerDTO.getMinCredit(), customerDTO.getMaxCredit()));
        }


        if (customerDTO.getMinCreationDate() != null && customerDTO.getMaxCreationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(customerRoot.get("registeryDate"),
                                    customerDTO.getMinCreationDate(),
                                    customerDTO.getMaxCreationDate()));
        }
    }

    @Override
    public Long viewCredit(Long customerId) {
        Customer customer = findById(customerId);
        return customer.getCredit();
    }

    @Override
    public int viewNumberOfRegisteredOrders(Long customerId) {
        if (!customerRepository.existsById(customerId))
            throw new NotFoundException("This customer was not found!");
        int count = orderService.numberOfSubmitOrders(customerId);
        if (count == 0)
            throw new CustomerDoesNotHaveRegisteredOrder("This customer does not have a registered order!");
        return count;
    }


    @Override
    public int viewNumberOfRegisteredOrdersByOrderStatus(CustomerOrdersStatusDTO customerOrdersStatusDTO) {
        if (!customerRepository.existsById(customerOrdersStatusDTO.getCustomerId()))
            throw new NotFoundException("This customer was not found!");
        int count = orderService.numberOfSubmitOrdersByOrderStatus(customerOrdersStatusDTO.getCustomerId(), customerOrdersStatusDTO.getOrderStatus());
        if (count == 0)
            throw new CustomerDoesNotHaveRegisteredOrder("This customer does not have a registered order!");
        return count;
    }
}
