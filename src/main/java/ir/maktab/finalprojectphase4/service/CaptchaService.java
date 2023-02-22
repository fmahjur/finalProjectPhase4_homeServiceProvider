package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.exception.ReCaptchaInvalidException;

public interface CaptchaService {
    default void processResponse(final String response) throws ReCaptchaInvalidException {}

    default void processResponse(final String response, String action) throws ReCaptchaInvalidException {}

    String getReCaptchaSite();

    String getReCaptchaSecret();

}
