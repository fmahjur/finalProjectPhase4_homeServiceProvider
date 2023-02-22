package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.model.Token;

import java.util.Optional;

public interface TokenService {
    Optional<Token> getToken(String token);
    void setConfirmedAt(String token);
}
