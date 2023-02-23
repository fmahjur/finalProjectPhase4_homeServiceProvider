package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.FilterOrderDTO;
import ir.maktab.finalprojectphase4.data.dto.request.OfferRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterOrderResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.mapper.OrderMapper;
import ir.maktab.finalprojectphase4.data.model.*;
import ir.maktab.finalprojectphase4.data.repository.OrderRepository;
import ir.maktab.finalprojectphase4.exception.*;
import ir.maktab.finalprojectphase4.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SubServiceServiceImpl subServiceService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Orders orders) {
        if (orders.getWorkStartDate().isBefore(LocalDateTime.now()))
            throw new WorkStartDateException("work can not start before now!");
        if ((orders.getCustomerProposedPrice() >= orders.getSubService().getBasePrice()))
            throw new InvalidProposedPriceException("The proposed price cannot be less than the base price!");
        orderRepository.save(orders);
    }

    @Override
    public void remove(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("not Found!"));
        orders.setDeleted(true);
        orderRepository.save(orders);
    }

    @Override
    public void update(Orders orders) {
        if (orders.getWorkStartDate().isBefore(LocalDateTime.now()))
            throw new WorkStartDateException("work can not start before now!");
        if ((orders.getCustomerProposedPrice() >= orders.getSubService().getBasePrice()))
            throw new InvalidProposedPriceException("The proposed price cannot be less than the base price!");
        orderRepository.save(orders);
    }

    @Override
    public void receivedNewOffer(Expert expert, Orders order) {
        order.setOrderStatus(OrderStatus.WAITING_FOR_CHOSE_EXPERT);
        orderRepository.save(order);
    }

    public Orders findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("not found!"));
    }

    @Override
    public List<OrderResponseDTO> selectAll() {
        List<Orders> ordersList = orderRepository.findAll();
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Orders orders : ordersList)
            orderResponseDTOList.add(OrderMapper.INSTANCE.modelToResponseDto(orders));
        return orderResponseDTOList;
    }

    @Override
    public List<OrderResponseDTO> selectAllOrderBySubServiceAndOrderStatus(String subServiceName) {
        SubService subService = subServiceService.findByName(subServiceName);
        List<Orders> allBySubService = orderRepository.findAllBySubService(
                OrderStatus.WAITING_FOR_EXPERTS_OFFER, OrderStatus.WAITING_FOR_CHOSE_EXPERT, subService);
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Orders orders : allBySubService)
            orderResponseDTOList.add(OrderMapper.INSTANCE.modelToResponseDto(orders));
        return orderResponseDTOList;
    }

    @Override
    public List<OrderResponseDTO> selectAllCustomersOrders(Customer customer) {
        List<Orders> allOrdersByCustomer = orderRepository.findAllByCustomer(customer);
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Orders orders : allOrdersByCustomer)
            orderResponseDTOList.add(OrderMapper.INSTANCE.modelToResponseDto(orders));
        return orderResponseDTOList;
    }

    @Override
    public List<OrderResponseDTO> selectAllCustomersOrdersByOrderStatus(Customer customer, OrderStatus orderStatus) {
        List<Orders> allOrdersByCustomer = orderRepository.findAllByCustomerAndOrderStatus(customer, orderStatus);
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Orders orders : allOrdersByCustomer)
            orderResponseDTOList.add(OrderMapper.INSTANCE.modelToResponseDto(orders));
        if (orderResponseDTOList.size() == 0)
            throw new NotFoundException("You do not have an order in " + orderStatus + "!");
        return orderResponseDTOList;
    }

    @Override
    public OrderResponseDTO getOrderDetail(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("This order was not found!"));
        return OrderMapper.INSTANCE.modelToResponseDto(order);
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        orderRepository.changeOrderStatus(orderId, orderStatus);
    }

    @Override
    public List<FilterOrderResponseDTO> ordersFilter(FilterOrderDTO filterOrderDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> orderCriteriaQuery = criteriaBuilder.createQuery(Orders.class);
        Root<Orders> orderRoot = orderCriteriaQuery.from(Orders.class);

        createFilters(filterOrderDTO, predicateList, criteriaBuilder, orderRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        orderCriteriaQuery.select(orderRoot).where(predicates);
        List<Orders> resultList = entityManager.createQuery(orderCriteriaQuery).getResultList();

        List<FilterOrderResponseDTO> filterOrderResponseDTOS = new ArrayList<>();
        for (Orders order: resultList)
            filterOrderResponseDTOS.add(OrderMapper.INSTANCE.modelToFilterResponseDto(order));
        return filterOrderResponseDTOS;
    }

    private void createFilters(FilterOrderDTO filterOrderDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Orders> orderRoot) {
        if (filterOrderDTO.getAddress() != null) {
            String address = "%" + filterOrderDTO.getAddress() + "%";
            predicateList.add(criteriaBuilder.like(orderRoot.get("address"), address));
        }
        if (filterOrderDTO.getDescription() != null) {
            String description = "%" + filterOrderDTO.getDescription() + "%";
            predicateList.add(criteriaBuilder.like(orderRoot.get("description"), description));
        }
        if (filterOrderDTO.getOrderStatus() != null) {
            predicateList.add(criteriaBuilder.equal(orderRoot.get("orderStatus"), filterOrderDTO.getOrderStatus()));
        }
        if (filterOrderDTO.getSubServiceId() != null) {
            predicateList.add(criteriaBuilder.equal(orderRoot.get("subServiceId"), filterOrderDTO.getSubServiceId()));
        }

        if (filterOrderDTO.getMinProposedPrice() == null && filterOrderDTO.getMaxProposedPrice() != null) {
            predicateList.add(criteriaBuilder.lt(orderRoot.get("proposedPrice"), filterOrderDTO.getMaxProposedPrice()));
        }
        if (filterOrderDTO.getMinProposedPrice() != null && filterOrderDTO.getMaxProposedPrice() == null) {
            predicateList.add(criteriaBuilder.gt(orderRoot.get("proposedPrice"), filterOrderDTO.getMinProposedPrice()));
        }
        if (filterOrderDTO.getMinProposedPrice() != null && filterOrderDTO.getMaxProposedPrice() != null) {
            predicateList.add(criteriaBuilder.between(orderRoot.get("proposedPrice"), filterOrderDTO.getMinProposedPrice(), filterOrderDTO.getMaxProposedPrice()));
        }

        if (filterOrderDTO.getMinOrderRegistrationDate() != null && filterOrderDTO.getMaxOrderRegistrationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(orderRoot.get("orderRegistrationDate"),
                                    filterOrderDTO.getMinOrderRegistrationDate(),
                                    filterOrderDTO.getMaxOrderRegistrationDate()));
        }
        if (filterOrderDTO.getMinDurationOfWork() != null && filterOrderDTO.getMaxDurationOfWork() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(orderRoot.get("durationOfWork"),
                                    filterOrderDTO.getMinDurationOfWork(),
                                    filterOrderDTO.getMaxDurationOfWork()));
        }
    }

    @Override
    public int numberOfSubmitOrders(Long customerId) {
        return orderRepository.numberOfSubmitOrders(customerId);
    }

    @Override
    public int numberOfSubmitOrdersByOrderStatus(Long customerId, OrderStatus orderStatus) {
        return orderRepository.numberOfSubmitOrdersByOrderStatus(customerId, orderStatus);
    }
}
