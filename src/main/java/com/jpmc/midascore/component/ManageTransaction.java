package com.jpmc.midascore.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;

@Component
public class ManageTransaction{
    static final Logger logger = LoggerFactory.getLogger(ManageTransaction.class);
    DatabaseConduit databaseConduit;
    IncentiveInfo IncentiveInfo;


    public ManageTransaction(DatabaseConduit databaseConduit, IncentiveInfo IncentiveInfo) {
        this.databaseConduit = databaseConduit;
        this.IncentiveInfo = IncentiveInfo;
    }

    public void handleTransaction(Transaction transaction) {
        try {
            if (databaseConduit.validCheck(transaction)) {
                Incentive incentive = IncentiveInfo.getIncentive(transaction);
                transaction.setIncentive(incentive.getAmount());
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