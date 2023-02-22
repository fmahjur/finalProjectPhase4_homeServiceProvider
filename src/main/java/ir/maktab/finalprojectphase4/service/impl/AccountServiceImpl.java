package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.model.Account;
import ir.maktab.finalprojectphase4.data.repository.AccountRepository;
import ir.maktab.finalprojectphase4.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        try {
            return accountRepository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
