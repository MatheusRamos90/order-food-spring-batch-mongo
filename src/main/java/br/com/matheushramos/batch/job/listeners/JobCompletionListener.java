package br.com.matheushramos.batch.job.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {

    private JobExecution lastJobExecution;

    @Override
    public void afterJob(JobExecution jobExecution) {
        this.lastJobExecution = jobExecution;

        String jobId = jobExecution.getJobParameters().getString("jobId");

        Date start = jobExecution.getCreateTime();
        Date end = jobExecution.getEndTime();

        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("========== JOB FINISHED =======");
            log.info("JobId      : {}", jobId);
            log.info("Start Date: {}", start);
            log.info("End Date: {}", end);
            log.info("==============================");
        }

    }

    public JobExecution getLastJobExecution() {
        return this.lastJobExecution;
    }
}
