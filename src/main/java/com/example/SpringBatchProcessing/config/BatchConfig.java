package com.example.SpringBatchProcessing.config;

import com.example.SpringBatchProcessing.entity.Customer;
import com.example.SpringBatchProcessing.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Autowired
    private CustomerRepository customerRespository;


//    creating Reader
    @Bean
    public FlatFileItemReader<Customer> customerReader(){
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        // set the path of file
        itemReader.setResource(new FileSystemResource("src/main/resources/Mall_Customers.csv"));
        //set the name
        itemReader.setName("csv_reader");
        // skip first line of csv file where header is present
        itemReader.setLinesToSkip(1);
        //each line is one customer object
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        //read lines and return data
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("CustomerID","Genre","Age","Annual_Income","Spending_Score");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer((lineTokenizer));
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


//    Processor
    @Bean
    public CustomerProcessor customerProcessor(){
        return new CustomerProcessor();
    }

//    Create writer
    @Bean
    public RepositoryItemWriter<Customer> customerWriter(){
        RepositoryItemWriter<Customer> repositoryWriter = new RepositoryItemWriter<>();
        repositoryWriter.setRepository(customerRespository);
        repositoryWriter.setMethodName("save");

        return repositoryWriter;
    }


    // create Step

    @Bean
    public Step step(JobRepository jobRepository,PlatformTransactionManager transactionManager){
        return new StepBuilder("step1",jobRepository).
                <Customer,Customer> chunk(5, transactionManager)
                .reader(customerReader()).processor(customerProcessor())
                .writer(customerWriter()).build();
    }

    //create job
    @Bean
    public Job job(JobRepository jobRepository, Step step1){
        return new JobBuilder("customer_job", jobRepository).flow(step1).end().build();
    }





}
