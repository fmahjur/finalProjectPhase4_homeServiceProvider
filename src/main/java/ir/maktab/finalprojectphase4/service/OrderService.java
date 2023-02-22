package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.OfferRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.model.Customer;
import ir.maktab.finalprojectphase4.data.model.Orders;

import java.util.List;

public interface OrderService {
    void add(Orders orders);

    void remove(Long orderId);

    void update(Orders orders);

    void receivedNewOffer(OfferRequestDTO offerRequestDTO);

    List<OrderResponseDTO> selectAll();

    List<OrderResponseDTO> selectAllOrderBySubServiceAndOrderStatus(String subServiceName);

    List<OrderResponseDTO> selectAllCustomersOrders(Customer customer);

    List<OrderResponseDTO> selectAllCustomersOrdersByOrderStatus(Customer customer, OrderStatus orderStatus);

    OrderResponseDTO getOrderDetail(Long orderId);

}
