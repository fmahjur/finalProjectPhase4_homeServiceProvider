package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.data.enums.Role;
import ir.maktab.finalprojectphase4.data.model.Account;
import ir.maktab.finalprojectphase4.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findByUsername(String username) {
        try {
            return accountRepository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int enableAppUser(String email, Role role) {
        return accountRepository.enableAccount(email, role);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
