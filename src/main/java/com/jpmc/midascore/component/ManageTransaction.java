package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

@Component
public class ManageTransaction{
    static final Logger logger = LoggerFactory.getLogger(ManageTransaction.class);
    DatabaseConduit databaseConduit;

    public ManageTransaction(DatabaseConduit databaseConduit){
        this.databaseConduit = databaseConduit;
    }

    public void handleTransaction(Transaction transaction) {
        try {
            if (databaseConduit.validCheck(transaction)) {
                databaseConduit.save(transaction);
                logger.info("Transaction processed successfully for user ID: {}", transaction.getSenderId());
            } else {
                logger.warn("Transaction validation failed for user ID: {}", transaction.getSenderId());
            }
        } catch (RuntimeException e) {
            logger.error("Error processing transaction for user ID: {} - {}", 
                transaction.getSenderId(), e.getMessage());
            // Don't rethrow - this allows Kafka to move to the next message
        }
    }
}