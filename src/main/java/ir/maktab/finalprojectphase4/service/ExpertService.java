package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.ExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.data.model.Offer;

import java.util.List;

public interface ExpertService {
    String signUp(Expert expert);

    void remove(Long expertId);

    void update(Expert expert);

    Expert findByEmail(String email);

    Expert findById(Long id);

    Expert findByUsername(String expertUsername);

    double getExpertRate(Long expertId);

    void addSubServiceToExpert(ExpertSubServiceDTO expertSubServiceDTO);

    void removeSubServiceFromExpert(ExpertSubServiceDTO expertSubServiceDTO);

    void receivedNewComment(CommentRequestDTO commentRequestDTO);

    List<ExpertResponseDTO> selectAll();

    List<ExpertResponseDTO> selectAllAvailableExpert();

    void changePassword(ChangePasswordDTO changePasswordDTO);

    List<ExpertResponseDTO> selectExpertByExpertStatus(ExpertStatus expertStatus);

    void submitAnOffer(Long expertID, OfferRequestDTO offerRequestDTO);

    byte[] getImage(Long id);

    void updateCredit(Long expertId, Long newCredit);

    void changeExpertAccountActivation(Long expertId, boolean isActive);

    List<FilterExpertResponseDTO> expertsFilter(FilterExpertDTO expertDTO);

    List<OfferResponseDTO> showAllOfferDoByExpert(Long expertId);

    List<OfferResponseDTO> showAllExpertOffersWaiting(Long expertId);

    List<OfferResponseDTO> showAllExpertOffersAccepted(Long expertId);

    List<OfferResponseDTO> showAllExpertOffersRejected(Long expertId);

    List<Offer> showOfferHistory(Long expertId);

    List<OrderResponseDTO> showOrderHistory(Long expertId);

    List<OrderResponseDTO> showOrderHistoryByOrderStatus(Long expertId, OrderStatus orderStatus);

    Long showCredit(Long expertId);

    int viewNumberOfRegisteredOffers(Long expertId);

    int viewNumberOfRegisteredOffersByOfferStatus(ExpertOffersStatusDTO expertOffersStatusDTO);
}
