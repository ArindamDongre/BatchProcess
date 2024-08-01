package com.example.SpringBatchProcessing.controller;

import com.example.SpringBatchProcessing.entity.Customer;
import com.example.SpringBatchProcessing.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public List<Customer> loadCsvToDb() throws Exception{

        JobParameters jobParams = new JobParametersBuilder()
                .addLong("Start-At", System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(job, jobParams);
        return customerRepository.findAll();
    }
}
