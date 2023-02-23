package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.ExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import ir.maktab.finalprojectphase4.data.enums.OfferStatus;
import ir.maktab.finalprojectphase4.data.enums.OrderStatus;
import ir.maktab.finalprojectphase4.data.enums.Role;
import ir.maktab.finalprojectphase4.data.mapper.CommentMapper;
import ir.maktab.finalprojectphase4.data.mapper.ExpertMapper;
import ir.maktab.finalprojectphase4.data.mapper.OrderMapper;
import ir.maktab.finalprojectphase4.data.model.*;
import ir.maktab.finalprojectphase4.data.repository.ExpertRepository;
import ir.maktab.finalprojectphase4.exception.*;
import ir.maktab.finalprojectphase4.service.EmailSenderService;
import ir.maktab.finalprojectphase4.service.ExpertService;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;

    private final SubServiceServiceImpl subServiceService;
    private final OrderServiceImpl orderService;
    private final OfferServiceImpl offerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenServiceImpl tokenService;
    private final EmailSenderService emailSenderService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String signUp(Expert expert) {
        if (expertRepository.existsByEmail(expert.getEmail()))
            throw new DuplicateEmailException("this email already exist!");
        if (expertRepository.existsByUsername(expert.getUsername()))
            throw new DuplicateUsernameException("this username already exist!");

        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setRate(0);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, expert, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
        confirmationToken.setToken(UUID.randomUUID().toString());
        tokenService.add(confirmationToken);

        SimpleMailMessage mailMessage = emailSenderService.createEmail(expert.getEmail(), confirmationToken.getToken(), "expert");
        emailSenderService.sendEmail(mailMessage);

        expertRepository.save(expert);

        return token;

    }

    @Override
    public void remove(Long expertId) {
        if (!expertRepository.existsById(expertId))
            throw new NotFoundException("This expert does not exist!");
        expertRepository.updateIsDeletedFlag(expertId);
    }

    @Override
    public void update(Expert expert) {
        expertRepository.save(expert);
    }

    @Override
    public Expert findByEmail(String email) {
        return expertRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("This expert does not exist!"));
    }

    @Override
    public Expert findById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new NotFoundException("This expert does not exist!"));
    }

    @Override
    public Expert findByUsername(String expertUsername) {
        return expertRepository.findByUsername(expertUsername).orElseThrow(() -> new NotFoundException("This expert does not exist!"));
    }

    @Override
    public double getExpertRate(Long expertId) {
        return findById(expertId).getRate();
    }

    @Override
    public void addSubServiceToExpert(ExpertSubServiceDTO expertSubServiceDTO) {
        Expert expert = findById(expertSubServiceDTO.getExpertId());
        if (!expert.getExpertStatus().equals(ExpertStatus.ACCEPTED))
            throw new ExpertStatusException("This expert does not accepted!");

        for (SubService subService: expert.getSubServices())
            if (subService.getId().equals(expertSubServiceDTO.getSubServiceId()))
                throw new DuplicateExpertSubServiceException("You have already registered this service for this expert!");

        SubService subService = subServiceService.findById(expertSubServiceDTO.getSubServiceId());
        expert.addSubService(subService);
        expertRepository.save(expert);
    }

    @Override
    public void removeSubServiceFromExpert(ExpertSubServiceDTO expertSubServiceDTO) {
        Expert expert = findById(expertSubServiceDTO.getExpertId());
        SubService subService = subServiceService.findById(expertSubServiceDTO.getSubServiceId());
        if (!expert.getSubServices().contains(subService))
            throw new ExpertSubServiceException("This Expert does not have this subService!");
        expert.deleteSubService(subService);
        expertRepository.save(expert);
    }

    @Override
    public void receivedNewComment(CommentRequestDTO commentRequestDTO) {
        Expert expert = findById(commentRequestDTO.getExpertId());
        Orders order = orderService.findById(commentRequestDTO.getOrderId());
        Comment comment = CommentMapper.INSTANCE.requestDtoToModel(commentRequestDTO);
        expert.getComments().add(comment);
        expert.setRate();
        order.setComment(comment);
        orderService.update(order);
        expertRepository.save(expert);
    }

    @Override
    public List<ExpertResponseDTO> selectAll() {
        List<Expert> expertList = expertRepository.findAll();
        List<ExpertResponseDTO> expertResponseDTOList = new ArrayList<>();
        for (Expert expert : expertList)
            expertResponseDTOList.add(ExpertMapper.INSTANCE.modelToResponseDto(expert));
        return expertResponseDTOList;
    }

    @Override
    public List<ExpertResponseDTO> selectAllAvailableExpert() {
        List<Expert> expertList = expertRepository.findAllByDeletedIs(false);
        List<ExpertResponseDTO> expertResponseDTOList = new ArrayList<>();
        for (Expert expert : expertList)
            expertResponseDTOList.add(ExpertMapper.INSTANCE.modelToResponseDto(expert));
        return expertResponseDTOList;
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        if (changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword()))
            throw new ChangePasswordException("oldPassword and newPassword can not be same!");
        else if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new ChangePasswordException("newPassword and confirmNewPassword must be same!");
        Expert expert = findByUsername(changePasswordDTO.getUsername());
        expert.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        expertRepository.save(expert);
    }

    @Override
    public List<ExpertResponseDTO> selectExpertByExpertStatus(ExpertStatus expertStatus) {
        List<Expert> expertList = expertRepository.findAllByExpertStatus(expertStatus);
        List<ExpertResponseDTO> expertResponseDTOList = new ArrayList<>();
        for (Expert expert : expertList)
            expertResponseDTOList.add(ExpertMapper.INSTANCE.modelToResponseDto(expert));
        return expertResponseDTOList;
    }

    public List<OrderResponseDTO> showRelatedOrdersAvailableForExpert(Long expertId) {
        Expert expert = findById(expertId);
        List<OrderResponseDTO> orderResponseList = new ArrayList<>();
        expert.getSubServices().forEach(subService -> {
            List<OrderResponseDTO> orderResponseDTOList = orderService.selectAllOrderBySubServiceAndOrderStatus(subService.getName());
            orderResponseList.addAll(orderResponseDTOList);
        });
        return orderResponseList;
    }

    @Override
    public void submitAnOffer(Long expertID, OfferRequestDTO offerRequestDTO) {
        Expert expert = findById(expertID);
        Orders order = orderService.findById(offerRequestDTO.getOrderId());
        if (!(order.getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERTS_OFFER) ||
                order.getOrderStatus().equals(OrderStatus.WAITING_FOR_CHOSE_EXPERT)))
            throw new OrderStatusException("This order accepted by an other expert!");

        for (Offer offer: order.getOffers())
            if (offer.getExpert().equals(expert))
                throw new DuplicateOfferException("You have already submitted an offer for this order!");

        if (!(offerRequestDTO.getOfferPrice() >= order.getSubService().getBasePrice()))
            throw new InvalidProposedPriceException("The proposed price cannot be less than the base price!");

        if (offerRequestDTO.getDurationOfWork() <= 0) {
            throw new IncorrectInformationException("Your offer must include the duration of the work");
        }

        Offer offer = new Offer(expert,
                order,
                LocalDateTime.now(),
                offerRequestDTO.getOfferPrice(),
                offerRequestDTO.getProposedStartDate(),
                offerRequestDTO.getDurationOfWork(),
                offerRequestDTO.getProposedStartDate(),
                OfferStatus.WAITING);

        offerService.add(offer);
        orderService.receivedNewOffer(expert, order);
    }

    @Override
    public byte[] getImage(Long id) {
        Optional<Expert> getExpert = expertRepository.findById(id);
        byte[] personalPhoto = getExpert.get().getPersonalPhoto();
        return personalPhoto;
    }

    @Override
    public void updateCredit(Long expertId, Long newCredit) {
        expertRepository.updateCredit(expertId, newCredit);
    }

    @Override
    public void changeExpertAccountActivation(Long expertId, boolean isActive) {
        expertRepository.changeEnabled(expertId, isActive);
    }

    @Override
    public List<FilterExpertResponseDTO> expertsFilter(FilterExpertDTO expertDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Expert> expertCriteriaQuery = criteriaBuilder.createQuery(Expert.class);
        Root<Expert> expertRoot = expertCriteriaQuery.from(Expert.class);
        createFilters(expertDTO, predicateList, criteriaBuilder, expertRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        expertCriteriaQuery.select(expertRoot).where(predicates);
        List<Expert> resultList = entityManager.createQuery(expertCriteriaQuery).getResultList();
        List<FilterExpertResponseDTO> filterExpertResponseDTOList = new ArrayList<>();
        for (Expert expert : resultList)
            filterExpertResponseDTOList.add(ExpertMapper.INSTANCE.modelToFilterResponseDto(expert));
        return filterExpertResponseDTOList;
    }

    private void createFilters(FilterExpertDTO expertDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Expert> expertRoot) {
        if (expertDTO.getFirstname() != null) {
            String firstname = "%" + expertDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("firstname"), firstname));
        }
        if (expertDTO.getLastname() != null) {
            String lastname = "%" + expertDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("lastname"), lastname));
        }
        if (expertDTO.getEmail() != null) {
            String email = "%" + expertDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("email"), email));
        }
        if (expertDTO.getUsername() != null) {
            String username = "%" + expertDTO.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("username"), username));
        }
        if (expertDTO.getEnabled() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("enabled"), expertDTO.getEnabled()));
        }
        if (expertDTO.getExpertStatus() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("expertStatus"), expertDTO.getExpertStatus()));
        }

        if (expertDTO.getRate() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("rate"), expertDTO.getRate()));
        }
        if (expertDTO.getMinRate() == null && expertDTO.getMaxRate() != null) {
            predicateList.add(criteriaBuilder.lt(expertRoot.get("rate"), expertDTO.getMaxRate()));
        }
        if (expertDTO.getMinRate() != null && expertDTO.getMaxRate() == null) {
            predicateList.add(criteriaBuilder.gt(expertRoot.get("rate"), expertDTO.getMinRate()));
        }
        if (expertDTO.getMinRate() != null && expertDTO.getMaxRate() != null) {
            predicateList.add(criteriaBuilder.between(expertRoot.get("rate"), expertDTO.getMinRate(), expertDTO.getMaxRate()));
        }

        if (expertDTO.getCredit() != null ) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("credit"), expertDTO.getCredit()));
        }
        if (expertDTO.getMinCredit() == null && expertDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.lt(expertRoot.get("credit"), expertDTO.getMaxCredit()));
        }
        if (expertDTO.getMinCredit() != null && expertDTO.getMaxCredit() == null) {
            predicateList.add(criteriaBuilder.gt(expertRoot.get("credit"), expertDTO.getMinCredit()));
        }
        if (expertDTO.getMinCredit() != null && expertDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.between(expertRoot.get("credit"), expertDTO.getMinCredit(), expertDTO.getMaxCredit()));
        }

        if (expertDTO.getMinCreationDate() != null && expertDTO.getMaxCreationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(expertRoot.get("registeryDate"),
                                    expertDTO.getMinCreationDate(),
                                    expertDTO.getMaxCreationDate()));
        }
    }

    @Override
    public List<OfferResponseDTO> showAllOfferDoByExpert(Long expertId) {
        Expert expert = findById(expertId);
        return offerService.selectAllByExpert(expert);
    }

    @Override
    public List<OfferResponseDTO> showAllExpertOffersWaiting(Long expertId) {
        Expert expert = findById(expertId);
        return offerService.selectAllExpertOffersWaiting(expert);
    }

    @Override
    public List<OfferResponseDTO> showAllExpertOffersAccepted(Long expertId) {
        Expert expert = findById(expertId);
        return offerService.selectAllExpertOffersAccepted(expert);
    }

    @Override
    public List<OfferResponseDTO> showAllExpertOffersRejected(Long expertId) {
        Expert expert = findById(expertId);
        return offerService.selectAllExpertOffersRejected(expert);
    }

    @Override
    public List<Offer> showOfferHistory(Long expertId) {
        Expert expert = findById(expertId);
        return offerService.selectOfferByExpertIdAndIsAccept(expert);
    }

    @Override
    public List<OrderResponseDTO> showOrderHistory(Long expertId) {
        List<OrderResponseDTO> orders = new ArrayList<>();
        List<Offer> expertOffers = showOfferHistory(expertId);
        expertOffers.forEach(offer ->
                orders.add(OrderMapper.INSTANCE.modelToResponseDto(offer.getOrder())));
        return orders;
    }

    @Override
    public List<OrderResponseDTO> showOrderHistoryByOrderStatus(Long expertId, OrderStatus orderStatus) {
        List<OrderResponseDTO> orders = new ArrayList<>();
        List<Offer> expertOffers = showOfferHistory(expertId);
        expertOffers.stream()
                .filter(offer -> offer.getOrder().getOrderStatus().equals(orderStatus))
                .forEach(offer -> {
                    orders.add(OrderMapper.INSTANCE.modelToResponseDto(offer.getOrder()));
                });
        return orders;
    }

    @Override
    public Long showCredit(Long expertId) {
        Expert expert = findById(expertId);
        return expert.getCredit();
    }

    @Override
    public int viewNumberOfRegisteredOffers(Long expertId) {
        if (!expertRepository.existsById(expertId))
            throw new NotFoundException("This expert was not found!");
        int count = offerService.numberOfSubmitOffers(expertId);
        if (count == 0)
            throw new ExpertDoesNotHaveRegisteredOffer("This expert does not have a registered offer!");
        return count;
    }


    @Override
    public int viewNumberOfRegisteredOffersByOfferStatus(ExpertOffersStatusDTO expertOffersStatusDTO) {
        if (!expertRepository.existsById(expertOffersStatusDTO.getExpertId()))
            throw new NotFoundException("This expert was not found!");
        int count = offerService.numberOfSubmitOffersByOfferStatus(expertOffersStatusDTO.getExpertId(), expertOffersStatusDTO.getOfferStatus());
        if (count == 0)
            throw new ExpertDoesNotHaveRegisteredOffer("This expert does not have a registered offer!");
        return count;
    }
}
