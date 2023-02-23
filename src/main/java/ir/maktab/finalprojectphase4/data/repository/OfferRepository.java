package ir.maktab.finalprojectphase4.data.repository;

import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.data.model.Offer;
import ir.maktab.finalprojectphase4.data.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByOrderIs(Orders order);

    List<Offer> findAllByExpert(Expert expert);

    @Query("select o from Offer o where o.isDeleted= :isDeleted")
    List<Offer> findAllByDeletedIs(boolean isDeleted);

    @Query("select o from Offer o where o.order= :order order by o.offerPrice")
    List<Offer> findByOrdersSortByOfferPrice(Orders order);

    @Query("select o from Offer o where o.order= :order order by o.expert.rate")
    List<Offer> findByOrdersSortByExOrderByExpertRate(Orders order);

    @Query("select o from Offer o where o.order= :order order by o.offerPrice, o.expert.rate")
    List<Offer> findByOrdersSortByOfferPriceAndExpertRate(Orders order);

    List<Offer> findOffersByExpertAndOfferStatus(Expert expert, OfferStatus offerStatus);

    @Query("select count(o.expert.id) from Offer o where o.expert.id= :expertId")
    int numberOfSubmitOffers(Long expertId);

    @Query("select count(o.expert.id) from Offer o where o.expert.id= :expertId and o.offerStatus= :offerStatus")
    int numberOfSubmitOffersByOfferStatus(Long expertId, OfferStatus offerStatus);
}
