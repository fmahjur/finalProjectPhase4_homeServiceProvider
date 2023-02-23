package ir.maktab.finalprojectphase4.controller;

import ir.maktab.finalprojectphase4.data.dto.response.ResponseDTO;
import ir.maktab.finalprojectphase4.exception.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ControllerAdvisor {

    @ExceptionHandler(ChangePasswordException.class)
    ResponseEntity<String> handleException(ChangePasswordException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(CustomerDoesNotHaveRegisteredOrder.class)
    ResponseEntity<String> handleException(CustomerDoesNotHaveRegisteredOrder exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateBaseServiceException.class)
    ResponseEntity<String> handleException(DuplicateBaseServiceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    ResponseEntity<String> handleException(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateExpertSubServiceException.class)
    ResponseEntity<String> handleException(DuplicateExpertSubServiceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateOfferException.class)
    ResponseEntity<String> handleException(DuplicateOfferException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateSubServiceException.class)
    ResponseEntity<String> handleException(DuplicateSubServiceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    ResponseEntity<String> handleException(DuplicateUsernameException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ExpertActivationException.class)
    ResponseEntity<String> handleException(ExpertActivationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ExpertDoesNotHaveRegisteredOffer.class)
    ResponseEntity<String> handleException(ExpertDoesNotHaveRegisteredOffer exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ExpertStatusException.class)
    ResponseEntity<String> handleException(ExpertStatusException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ExpertSubServiceException.class)
    ResponseEntity<String> handleException(ExpertSubServiceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ImageFileException.class)
    ResponseEntity<String> handleException(ImageFileException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ImageSizeException.class)
    ResponseEntity<String> handleException(ImageSizeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IncorrectInformationException.class)
    ResponseEntity<String> handleException(IncorrectInformationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InsufficientFoundsException.class)
    ResponseEntity<String> handleException(InsufficientFoundsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidProposedPriceException.class)
    ResponseEntity<String> handleException(InvalidProposedPriceException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(NullScoreException.class)
    ResponseEntity<String> handleException(NullScoreException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(OfferAcceptedException.class)
    ResponseEntity<String> handleException(OfferAcceptedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(OrderStatusException.class)
    ResponseEntity<String> handleException(OrderStatusException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ReCaptchaInvalidException.class)
    ResponseEntity<String> handleException(ReCaptchaInvalidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ReCaptchaUnavailableException.class)
    ResponseEntity<String> handleException(ReCaptchaUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(RegistrationException.class)
    ResponseEntity<String> handleException(RegistrationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ScoreOutsideDefinedRangeException.class)
    ResponseEntity<String> handleException(ScoreOutsideDefinedRangeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(SendEmailFailedException.class)
    ResponseEntity<String> handleException(SendEmailFailedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(WorkStartDateException.class)
    ResponseEntity<String> handleException(WorkStartDateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleException(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<String> handleException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<ResponseDTO<Object>> handleException(BindException exception) {
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "The program has encountered an error").body(responseDTO);
    }

}