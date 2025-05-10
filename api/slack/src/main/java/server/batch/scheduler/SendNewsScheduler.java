package server.batch.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.SoftHashMap;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class SendNewsScheduler {

  private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
  private final Job sendNewsJob;
  private final JobLauncher jobLauncher;

  @Scheduled(fixedDelay = 60000)
  public void sendNewsJobRun()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    final Map<String, JobParameter<?>> stringJobParameterMap = new SoftHashMap<>();
    stringJobParameterMap.put("REQUEST_TIME", new JobParameter<>(LocalDateTime.now().format(DATETIME_FORMATTER), String.class));

    jobLauncher.run(sendNewsJob, new JobParameters(stringJobParameterMap));
  }
}
