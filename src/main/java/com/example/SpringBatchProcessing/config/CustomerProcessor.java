package com.example.SpringBatchProcessing.config;

import com.example.SpringBatchProcessing.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        if (item.getGenre() != null && item.getGenre().equalsIgnoreCase("male")) {
            double spendingEfficiency = ((double) item.getSpending_Score() / (item.getAnnual_Income() + 1)) * 100;
            System.out.println("Calculated Spending Efficiency: " + spendingEfficiency); // Log the value
            item.setSpending_Efficiency(spendingEfficiency);
            return item;
        }
        return null;
    }

}
