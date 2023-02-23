package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.OfferRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.data.model.Offer;

import java.util.List;

public interface OfferService {
    void add(Offer offer);

    void remove(OfferRequestDTO offerRequestDTO);

    void update(Offer offer);

    List<OfferResponseDTO> selectAllByOrder(Long orderId);

    List<OfferResponseDTO> selectAllByExpert(Expert expert);

    List<OfferResponseDTO> selectAllExpertOffersWaiting(Expert expert);

    List<OfferResponseDTO> selectAllExpertOffersAccepted(Expert expert);

    List<OfferResponseDTO> selectAllExpertOffersRejected(Expert expert);

    List<Offer> selectOfferByExpertIdAndIsAccept(Expert expert);

    int numberOfSubmitOffers(Long expertId);

    int numberOfSubmitOffersByOfferStatus(Long expertId, OfferStatus offerStatus);
}
