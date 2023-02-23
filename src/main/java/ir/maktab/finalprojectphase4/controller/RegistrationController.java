package ir.maktab.finalprojectphase4.controller;

import ir.maktab.finalprojectphase4.data.dto.request.UserRegistrationDTO;
import ir.maktab.finalprojectphase4.service.impl.RegistrationServiceImpl;
import ir.maktab.finalprojectphase4.validation.PictureValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationServiceImpl registrationService;

    @PostMapping("/customer")
    public String register(@RequestBody UserRegistrationDTO request) {
        return registrationService.register(request);
    }

    @PostMapping("/expert")
    public String register(@Valid @RequestParam String firstname,
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
        return registrationService.register(expertRegistrationDTO, expertPicture);
    }
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
