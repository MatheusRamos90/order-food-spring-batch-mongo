package br.com.matheushramos.batch.job.launcher;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
public class PaymentOrderJobLauncher {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    // Executa de 10 em 10 segundos
    @Scheduled(fixedRate = 10000)
    @SneakyThrows
    void launchJob() {
        log.info("Starting job...");

        JobParameters params = new JobParametersBuilder()
                .addLong("jobId", System.currentTimeMillis())
                .addDate("currentTime", new Date())
                .toJobParameters();

        this.jobLauncher.run(job, params);

        log.info("Stopping job...");
    }

    // Executa às 10h da manhã
//    @Scheduled(cron = "0 0 10 * * ?")
//    @SneakyThrows
//    public void runJobAt10AM() {
//        jobLauncher.run(job, new JobParameters());
//    }

    // Executa às 23h da noite
//    @SneakyThrows
//    @Scheduled(cron = "0 0 23 * * ?")
//    public void runJobAt11PM() {
//        jobLauncher.run(job, new JobParameters());
//    }
}
