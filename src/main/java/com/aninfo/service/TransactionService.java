package com.aninfo.service;
import com.aninfo.repository.AccountRepository;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactionsByAccount(Long cbu) {
        return transactionRepository.findByAccountCbu(cbu);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void deleteTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction != null) {
            Account account = transaction.getAccount();
            String transactionType = transaction.getType();

            if ("DEPOSIT".equals(transactionType)) {
                account.setBalance(account.getBalance() - transaction.getAmount());
            } else if ("WITHDRAW".equals(transactionType)) {
                account.setBalance(account.getBalance() + transaction.getAmount());
            }

            accountRepository.save(account);
            transactionRepository.deleteById(id);
        }
    }
}