package com.example.SpringBatchProcessing.entity;

//CustomerID,Genre,Age,Annual_Income_(k$),Spending_Score

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private int CustomerID;
    private String Genre;
    private int Age;
    private int Annual_Income;
    private int Spending_Score;
    private Double Spending_Efficiency;

}
