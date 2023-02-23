package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.dto.request.UserRegistrationDTO;

public interface RegistrationService {
    String register(UserRegistrationDTO customerRegistrationDTO);
    String register(UserRegistrationDTO expertRegistrationDTO, byte[] expertPicture);
    String confirmToken(String token);
    String buildEmail(String name, String link);
}
