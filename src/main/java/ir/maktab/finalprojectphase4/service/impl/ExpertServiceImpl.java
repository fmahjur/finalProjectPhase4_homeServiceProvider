package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.ExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.FilterExpertResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
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
import ir.maktab.finalprojectphase4.validation.PasswordValidator;
import ir.maktab.finalprojectphase4.validation.PictureValidator;
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
    private final TokenServiceImpl tokenService;
    private final EmailSenderService emailSenderService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(UserRegistrationDTO expertRegistrationDTO, byte[] expertPicture) {
        if (expertRepository.existsByEmail(expertRegistrationDTO.getEmail()))
            throw new DuplicateEmailException("this email already exist!");
        if (expertRepository.existsByUsername(expertRegistrationDTO.getUsername()))
            throw new DuplicateUsernameException("this username already exist!");
        PictureValidator.isValidImageSize(expertPicture);

        Expert expert = ExpertMapper.INSTANCE.registerDtoToModel(expertRegistrationDTO);
        expert.setPersonalPhoto(expertPicture);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setIsActive(false);
        expert.setRate(0);


        Token token = new Token(expert);
        token.setConfirmationToken(UUID.randomUUID().toString());
        tokenService.add(token);

        SimpleMailMessage mailMessage = emailSenderService.createEmail(expert.getEmail(), token.getConfirmationToken(), "expert");
        emailSenderService.sendEmail(mailMessage);

        expertRepository.save(expert);

    }

    @Override
    @Transactional
    public void confirmExpertAccount(String confirmationToken) {
        Optional<Token> token = tokenService.getToken(confirmationToken);
        if (token.isEmpty())
            throw new NotFoundException("token not found!");
        if (token.get().getAccount().getIsActive())
            throw new RegistrationException("this expert's account is currently active!");
        Expert expert = findByEmail(token.get().getAccount().getEmail());
        expert.setIsActive(true);
        expert.setExpertStatus(ExpertStatus.WAITING);
        update(expert);
    }

    @Override
    public void remove(Long expertId) {
        if(!expertRepository.existsById(expertId))
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
        if(!expert.getExpertStatus().equals(ExpertStatus.ACCEPTED))
            throw new ExpertStatusException("This expert does not accepted!");
        SubService subService = subServiceService.findById(expertSubServiceDTO.getSubServiceId());
        expert.addSubService(subService);
        expertRepository.save(expert);
    }

    @Override
    public void removeSubServiceFromExpert(ExpertSubServiceDTO expertSubServiceDTO) {
        Expert expert = findById(expertSubServiceDTO.getExpertId());
        SubService subService = subServiceService.findById(expertSubServiceDTO.getSubServiceId());
        if(!expert.getSubServices().contains(subService))
            throw new ExpertSubServiceException("this Expert does not have this subService!");
        expert.deleteSubService(subService);
        expertRepository.save(expert);
    }

    @Override
    public void receivedNewComment(CommentRequestDTO commentRequestDTO) {
        Expert expert = findById(commentRequestDTO.getExpertId());
        Comment comment = CommentMapper.INSTANCE.requestDtoToModel(commentRequestDTO);
        expert.getComments().add(comment);
        expert.setRate();
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
    public void login(LoginDTO loginDTO) {
        Expert expert = ExpertMapper.INSTANCE.loginDtoToModel(loginDTO);
        Optional<Expert> expertByUsername = expertRepository.findByUsername(expert.getUsername());
        if (expertByUsername.isPresent())
            if (!Objects.equals(expert.getPassword(), expertByUsername.get().getPassword()))
                throw new IncorrectInformationException("Username or Password is Incorrect!");
    }

    @Override
    public Expert changePassword(ChangePasswordDTO changePasswordDTO) {
        PasswordValidator.isValidNewPassword(changePasswordDTO.getPassword(),
                changePasswordDTO.getNewPassword(),
                changePasswordDTO.getConfirmNewPassword());
        Expert expert = findByUsername(changePasswordDTO.getUsername());
        expert.setPassword(changePasswordDTO.getPassword());
        return expertRepository.save(expert);
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
    public void submitAnOffer(OfferRequestDTO offerRequestDTO) {
        orderService.receivedNewOffer(offerRequestDTO);
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
        expertRepository.changeActivation(expertId, isActive);
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
        if (expertDTO.getIsActive() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("isActive"), expertDTO.getIsActive()));
        }
        if (expertDTO.getExpertStatus() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("expertStatus"), expertDTO.getExpertStatus()));
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
                            .between(expertRoot.get("creationDate"),
                                    LocalDateTime.parse(expertDTO.getMinCreationDate()),
                                    LocalDateTime.parse(expertDTO.getMaxCreationDate())));
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
    public List<Offer> showOfferHistory(Long expertId, boolean isAccept) {
        Expert expert = findById(expertId);
        return offerService.selectOfferByExpertIdAndIsAccept(expertId, isAccept);
    }

    @Override
    public List<OrderResponseDTO> showOrderHistory(Long expertId, boolean isAccept) {
        List<OrderResponseDTO> orders = new ArrayList<>();
        List<Offer> expertOffers = showOfferHistory(expertId, isAccept);
        expertOffers.forEach(offer ->
                orders.add(OrderMapper.INSTANCE.modelToResponseDto(offer.getOrders())));
        return orders;
    }

    @Override
    public List<OrderResponseDTO> showOrderHistory(Long expertId, boolean isAccept, OrderStatus orderStatus) {
        List<OrderResponseDTO> orders = new ArrayList<>();
        List<Offer> expertOffers = showOfferHistory(expertId, isAccept);
        expertOffers.stream()
                .filter(offer -> offer.getOrders().getOrderStatus().equals(orderStatus))
                .forEach(offer -> {
                    orders.add(OrderMapper.INSTANCE.modelToResponseDto(offer.getOrders()));
                });
        return orders;
    }

    @Override
    public Long viewCredit(Long expertId) {
        Expert expert = findById(expertId);
        return expert.getCredit();
    }
}
