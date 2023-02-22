package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.OfferRequestDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.mapper.OfferMapper;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.data.model.Offer;
import ir.maktab.finalprojectphase4.data.model.Orders;
import ir.maktab.finalprojectphase4.data.repository.OfferRepository;
import ir.maktab.finalprojectphase4.exception.NotFoundException;
import ir.maktab.finalprojectphase4.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final OrderServiceImpl orderService;

    @Override
    public void add(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public void remove(OfferRequestDTO offerRequestDTO) {
        Offer offer = OfferMapper.INSTANCE.requestDtoToModel(offerRequestDTO);
        offer.setDeleted(true);
        offerRepository.save(offer);
    }

    @Override
    public void update(Offer offer) {
        offerRepository.save(offer);
    }

    public Offer findById(Long offerId) {
        return offerRepository.findById(offerId).orElseThrow(() -> new NotFoundException("not found!"));
    }

    @Override
    public List<OfferResponseDTO> selectAllByOrder(Long orderId) {
        Orders order = orderService.findById(orderId);
        List<Offer> allOfferForOrder = offerRepository.findByOrdersSortByOfferPriceAndExpertRate(order);
        List<OfferResponseDTO> offerResponseDTOList = new ArrayList<>();
        for (Offer offer : allOfferForOrder)
            offerResponseDTOList.add(OfferMapper.INSTANCE.modelToResponseDto(offer));
        return offerResponseDTOList;
    }

    @Override
    public List<OfferResponseDTO> selectAllByExpert(Expert expert) {
        List<Offer> allOfferByExpert = offerRepository.findAllByExpert(expert);
        List<OfferResponseDTO> offerResponseDTOList = new ArrayList<>();
        for (Offer offer : allOfferByExpert)
            offerResponseDTOList.add(OfferMapper.INSTANCE.modelToResponseDto(offer));
        return offerResponseDTOList;
    }

    @Override
    public List<OfferResponseDTO> selectAllExpertOffersWaiting(Expert expert) {
        List<Offer> allExpertOffersAccepted = offerRepository.findOffersByExpertAndOfferStatus(expert, OfferStatus.WAITING);
        List<OfferResponseDTO> offerResponseDTOList = new ArrayList<>();
        for (Offer offer : allExpertOffersAccepted)
            offerResponseDTOList.add(OfferMapper.INSTANCE.modelToResponseDto(offer));
        return offerResponseDTOList;
    }

    @Override
    public List<OfferResponseDTO> selectAllExpertOffersAccepted(Expert expert) {
        List<Offer> allExpertOffersAccepted = offerRepository.findOffersByExpertAndOfferStatus(expert, OfferStatus.ACCEPTED);
        List<OfferResponseDTO> offerResponseDTOList = new ArrayList<>();
        for (Offer offer : allExpertOffersAccepted)
            offerResponseDTOList.add(OfferMapper.INSTANCE.modelToResponseDto(offer));
        return offerResponseDTOList;
    }

    @Override
    public List<OfferResponseDTO> selectAllExpertOffersRejected(Expert expert) {
        List<Offer> allExpertOffersAccepted = offerRepository.findOffersByExpertAndOfferStatus(expert, OfferStatus.REJECTED);
        List<OfferResponseDTO> offerResponseDTOList = new ArrayList<>();
        for (Offer offer : allExpertOffersAccepted)
            offerResponseDTOList.add(OfferMapper.INSTANCE.modelToResponseDto(offer));
        return offerResponseDTOList;
    }

    @Override
    public List<Offer> selectOfferByExpertIdAndIsAccept(Long expertId, boolean isAccept) {
        return offerRepository.findOfferByExpertIdAndIsAccept(expertId, isAccept);
    }
}
