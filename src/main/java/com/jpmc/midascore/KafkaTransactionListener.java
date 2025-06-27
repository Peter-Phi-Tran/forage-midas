package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.component.ManageTransaction;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaTransactionListener {

    ManageTransaction manageTransaction;
    public KafkaTransactionListener(ManageTransaction manageTransaction) {
        this.manageTransaction = manageTransaction;
    }
    
    @KafkaListener(topics = "${general.kafka-topic}")
    public void recieve(Transaction transaction) {
        manageTransaction.handleTransaction(transaction);
    }
}
