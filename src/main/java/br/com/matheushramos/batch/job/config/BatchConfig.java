package br.com.matheushramos.batch.job.config;

import br.com.matheushramos.batch.job.listeners.JobCompletionListener;
import br.com.matheushramos.batch.job.reader.OrderFoodReader;
import br.com.matheushramos.batch.model.Order;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job job(Step step, JobCompletionListener listener, JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(OrderFoodReader orderItemReader, ItemWriter<Order> orderItemWriter, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step").<Order, Order>chunk(10)
                .reader(orderItemReader)
                .writer(orderItemWriter)
                .build();
    }

    @Bean
    @SneakyThrows
    public OrderFoodReader orderItemReader(MongoTemplate mongoTemplate) {
        return new OrderFoodReader(mongoTemplate);
    }

    @Bean
    public ItemWriter<Order> orderItemWriter() {
        return orders -> orders.forEach(order -> log.info("Order: {}", order));
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils(JobRepository jobRepository) {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
        return jobLauncherTestUtils;
    }
}
