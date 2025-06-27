package com.jpmc.midascore.component;

import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepo;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class DatabaseConduit {
    UserRepository userRepository;
    TransactionRecordRepo transactionRecordRepo;    

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepo transactionRecordRepo) {
        this.userRepository = userRepository;
        this.transactionRecordRepo = transactionRecordRepo;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(Transaction transaction) {
        // Implement transaction saving logic here
        UserRecord sender = userQuery(transaction.getSenderId());
        UserRecord recipient = userQuery(transaction.getRecipientId());
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRecordRepo.save(transactionRecord);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        save(sender);
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
        save(recipient);
    }

    public float queryUserBalance(Long userId) {
        UserRecord userRecord = userQuery(userId);
        if (userRecord == null) {
            return 0;
        } else {
            return userRecord.getBalance();
        }
    }
    
    public UserRecord userQuery(long userId) {
        UserRecord user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        return user;
    }

    public UserRecord userQueryByName(String name) {
        UserRecord user = userRepository.findByName(name);
        if (user == null) {
            throw new RuntimeException("User not found with name: " + name);
        }
        return user;
    }

    public boolean validCheck(Transaction transaction){
        UserRecord sender = userQuery(transaction.getSenderId());
        UserRecord recipient = userQuery(transaction.getRecipientId());
        if (sender == null || recipient == null) {
            return false;
        }
        if (transaction.getAmount() <= 0 || sender.getBalance() < transaction.getAmount()) {
            return false;
        }
        return true;
    }
}
