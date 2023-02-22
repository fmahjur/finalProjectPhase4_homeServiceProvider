package ir.maktab.finalprojectphase4.service;

import ir.maktab.finalprojectphase4.data.model.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findByUsername(String username);
}
