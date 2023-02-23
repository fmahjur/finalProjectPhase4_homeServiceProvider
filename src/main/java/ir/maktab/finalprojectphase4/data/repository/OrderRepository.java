package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.model.Customer;
import ir.maktab.finalprojectphase4.data.model.Orders;
import ir.maktab.finalprojectphase4.data.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByCustomer(Customer customer);

    List<Orders> findAllByCustomerAndOrderStatus(Customer customer, OrderStatus orderStatus);

    @Query("select o from Orders o where (o.orderStatus=?1 or o.orderStatus =?2) and o.subService=?3")
    List<Orders> findAllBySubService(@Param("order_status") OrderStatus orderStatus,
                                     @Param("order_status") OrderStatus orderStatus1,
                                     @Param("sub_service") SubService subService);

    @Modifying
    @Query("update Orders o set o.orderStatus = :newOrderStatus where o.id = :orderId")
    void changeOrderStatus(Long orderId, OrderStatus newOrderStatus);

    @Query("select o from Orders o where o.isDeleted= :isDeleted")
    List<Orders> findAllByDeletedIs(boolean isDeleted);

    @Query("select count(o.customer.id) from Orders o where o.customer.id= :customerId")
    int numberOfSubmitOrders(Long customerId);

    @Query("select count(o.customer.id) from Orders o where o.customer.id= :customerId and o.orderStatus= :orderStatus")
    int numberOfSubmitOrdersByOrderStatus(Long customerId, OrderStatus orderStatus);

}
