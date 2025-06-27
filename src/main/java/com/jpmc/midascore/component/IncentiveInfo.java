package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveInfo {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public IncentiveInfo(RestTemplateBuilder restTemplateBuilder, @Value("${general.incentive-api-url}") String apiUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiUrl = apiUrl;
    }

    public Incentive getIncentive(Transaction transaction) {
        return restTemplate.postForObject(apiUrl, transaction, Incentive.class);
    }
}