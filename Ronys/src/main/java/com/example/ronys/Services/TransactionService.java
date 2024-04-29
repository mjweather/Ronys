package com.example.ronys.Services;

import com.example.ronys.Model.Transaction;
import com.example.ronys.Repository.TransactionRepository;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public Transaction addTransaction(Transaction transaction) {
        // You might add additional validation logic here (optional)
        return transactionRepository.save(transaction);
    }
}
