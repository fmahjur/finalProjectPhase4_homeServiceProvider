package ir.maktab.finalprojectphase4.controller;

import ir.maktab.finalprojectphase4.data.dto.request.*;
import ir.maktab.finalprojectphase4.data.dto.response.OfferResponseDTO;
import ir.maktab.finalprojectphase4.data.dto.response.OrderResponseDTO;
import ir.maktab.finalprojectphase4.data.mapper.OfferMapper;
import ir.maktab.finalprojectphase4.data.model.Expert;
import ir.maktab.finalprojectphase4.service.impl.ExpertServiceImpl;
import ir.maktab.finalprojectphase4.validation.PictureValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Expert")
@RequiredArgsConstructor
public class ExpertController {
    private final ExpertServiceImpl expertService;

    @PostMapping("/signup")
    @ResponseBody
    public void singUp(@Valid @RequestParam String firstname,
                       @RequestParam String lastname,
                       @RequestParam String email,
                       @RequestParam String username,
                       @RequestParam String password,
                       @RequestParam Long credit,
                       @RequestParam("imageFile") MultipartFile file) {

        UserRegistrationDTO expertRegistrationDTO = new UserRegistrationDTO(firstname, lastname, email, username, password, credit);
        PictureValidator.isValidImageFile(file.getOriginalFilename());
        byte[] expertPicture = new byte[0];
        try {
            expertPicture = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        expertService.add(expertRegistrationDTO, expertPicture);
    }

    @PostMapping("/login")
    @ResponseBody
    public void login(@Valid @RequestBody LoginDTO expertLoginDto) {
        expertService.login(expertLoginDto);
    }

    @PutMapping("/change-password")
    @ResponseBody
    public void changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        expertService.changePassword(changePasswordDTO);
    }

    @GetMapping("/show-related-orders")
    public List<OrderResponseDTO> viewOrdersRelatedToExpert(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showRelatedOrdersAvailableForExpert(authenticatedExpert.getId());
    }

    @PostMapping("/submit-offer-for-order")
    public void submitOfferForOrder(@RequestBody OfferRequestDTO offerRequestDTO) {
        expertService.submitAnOffer(offerRequestDTO);
    }

    @GetMapping("/show-expert-rate")
    public double viewExpertScore(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.getExpertRate(authenticatedExpert.getId());
    }

    @GetMapping("/show-all-offer-do-by-expert")
    public List<OfferResponseDTO> viewAllOfferDoByExpert(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showAllOfferDoByExpert(authenticatedExpert.getId());
    }

    @GetMapping("/show-all-expert-offers-waiting")
    public List<OfferResponseDTO> viewAllExpertOffersWaiting(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showAllExpertOffersWaiting(authenticatedExpert.getId());
    }

    @GetMapping("/show-all-expert-offers-accepted")
    public List<OfferResponseDTO> viewAllExpertOffersAccepted(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showAllExpertOffersAccepted(authenticatedExpert.getId());
    }

    @GetMapping("/show-all-expert-offers-rejected")
    public List<OfferResponseDTO> viewAllExpertOffersRejected(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showAllExpertOffersRejected(authenticatedExpert.getId());
    }

    @GetMapping("/show-offer-history/{isAccept}")
    public List<OfferResponseDTO> viewOfferHistory(@PathVariable boolean isAccept, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        List<OfferResponseDTO> offerDTOS = new ArrayList<>();
        expertService.showOfferHistory(authenticatedExpert.getId(), isAccept)
                .forEach(
                        offer -> offerDTOS.add(
                                OfferMapper.INSTANCE.modelToResponseDto(offer)));
        return offerDTOS;
    }

    @GetMapping("/show-order-history/{isAccept}")
    public List<OrderResponseDTO> viewOrderHistory(@PathVariable boolean isAccept, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showOrderHistory(authenticatedExpert.getId(), isAccept);
    }

    @PostMapping("/show-order-history-by-order-status")
    public List<OrderResponseDTO> viewOrderHistory(@RequestBody ExpertHistoryDTO expertHistoryDTO, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showOrderHistoryByOrderStatus(authenticatedExpert.getId(), expertHistoryDTO.isAccept(), expertHistoryDTO.getOrderStatus());
    }

    @GetMapping("/show-credit")
    public Long viewCredit(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.showCredit(authenticatedExpert.getId());
    }
}
