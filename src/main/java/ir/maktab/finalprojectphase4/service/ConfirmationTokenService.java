package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmedAt(String token);
}
