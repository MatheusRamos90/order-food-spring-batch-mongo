package br.com.matheushramos.batch.config;

import br.com.matheushramos.batch.dto.FoodData;
import br.com.matheushramos.batch.enums.StatusEnum;
import br.com.matheushramos.batch.job.config.BatchConfig;
import br.com.matheushramos.batch.job.listeners.JobCompletionListener;
import br.com.matheushramos.batch.job.reader.OrderFoodReader;
import br.com.matheushramos.batch.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

@ExtendWith(OutputCaptureExtension.class)
@DataMongoTest
@ActiveProfiles("test")
@Import({BatchConfig.class, JobCompletionListener.class, OrderFoodReader.class})
class BatchIntegrationTest {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncherTestUtils launchJob;

    @Autowired
    private JobCompletionListener listener;

    @Autowired
    private MongoTemplate mongoTemplate;

    private boolean testExecuted = false;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testBatchJob(CapturedOutput output) throws Exception {
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .foodData(List.of(FoodData.builder()
                        .name("Bread")
                        .price(BigDecimal.valueOf(20.6))
                        .refId("#2051").build()))
                .status(StatusEnum.SUCCESS)
                .build();

        if (!testExecuted) {
            this.mongoTemplate.insert(order, "order");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("jobId", System.currentTimeMillis())
                    .addDate("currentTime", new Date())
                    .toJobParameters();

            this.launchJob.launchJob(jobParameters);

            assertThat(this.listener.getLastJobExecution().getStatus()).isEqualTo(COMPLETED);

            assertThat(output).contains("Read orders. Total: 1");

            // control test execute once
            testExecuted = true;
        }
    }
}
