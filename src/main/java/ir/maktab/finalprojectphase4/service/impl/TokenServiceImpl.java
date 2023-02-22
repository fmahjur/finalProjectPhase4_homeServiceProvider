package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.model.Token;
import ir.maktab.finalprojectphase4.data.repository.ConfirmationTokenRepository;
import ir.maktab.finalprojectphase4.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void add(Token token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<Token> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }
    @Override
    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
