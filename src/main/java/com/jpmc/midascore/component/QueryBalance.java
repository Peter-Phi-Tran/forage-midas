package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class QueryBalance {
    private final DatabaseConduit databaseConduit;

    public QueryBalance(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @GetMapping(value = "/balance")
    public Balance getBalance(@RequestParam String userId) {
        float balance = databaseConduit.queryUserBalance(Long.parseLong(userId));
        return new Balance(balance);
    }
}