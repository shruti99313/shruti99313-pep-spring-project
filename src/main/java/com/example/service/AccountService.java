package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account acc) {
        if (acc.getUsername() == null || acc.getUsername().isBlank()
                || acc.getPassword() == null || acc.getPassword().length() < 4)
            throw new IllegalArgumentException();

        if (accountRepository.findByUsername(acc.getUsername()).isPresent())
            throw new IllegalStateException();

        return accountRepository.save(acc);
    }

    public Account login(Account acc) {
        Optional<Account> exist = accountRepository.findByUsername(acc.getUsername());
        if (exist.isPresent() && exist.get().getPassword().equals(acc.getPassword())) {
            return exist.get();
        } else {
            throw new SecurityException();
        }
    }
}
