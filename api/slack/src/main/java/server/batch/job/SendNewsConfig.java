package server.batch.job;

import api.subscriptions.domain.News;
import api.subscriptions.domain.NewsRepository;
import api.subscriptions.domain.Subscription;
import api.subscriptions.domain.SubscriptionRepository;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import server.service.SlackService;

@Configuration
@RequiredArgsConstructor
public class SendNewsConfig {

  private final SubscriptionRepository subscriptionRepository;
  private final NewsRepository newsRepository;
  private final SlackService slackService;
  private static final String JOB_NAME = "sendNewsJob";
  private static final String STEP_NAME = "sendNewsStep";
  private static final int CHUNK_SIZE = 10;

  @Bean
  public Job sendNewsJob(JobRepository jobRepository, Step sendNewsStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(sendNewsStep)
        .build();
  }

  @Bean
  @JobScope
  public Step sendNewsStep(JobRepository jobRepository, ItemReader subscriptionReader, ItemProcessor addNewsProcessor, ItemWriter sendNews,
      PlatformTransactionManager platformTransactionManager) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .chunk(CHUNK_SIZE, platformTransactionManager)
        .reader(subscriptionReader)
        .processor(addNewsProcessor)
        .writer(sendNews)
        .build();
  }

  @StepScope
  @Bean
  public RepositoryItemReader<Subscription> subscriptionReader() {
    System.out.println(Time.valueOf(LocalTime.now()));
    var now = LocalTime.now();

    return new RepositoryItemReaderBuilder<Subscription>()
        .name("subscriptionReader")
        .repository(subscriptionRepository)
        .methodName("findAllByPushAt")
        .pageSize(CHUNK_SIZE)
        .maxItemCount(CHUNK_SIZE)
        .arguments(Arrays.asList(Time.valueOf(LocalTime.of(now.getHour(), now.getMinute(), 0))))
        .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
        .build();
  }

  @StepScope
  @Bean
  public ItemProcessor<Subscription, NewsForUser> addNewsProcessor() {
    return item -> {
      var stocks = item.getSubscribingStocks();
      var newsList = newsRepository.findAllByDateAndStockIn(LocalDate.now(), stocks);

      return new NewsForUser(item.getMember().toMemberDto().userId(), newsList.stream().map(News::toNewsDto).toList());
    };
  }

  @StepScope
  @Bean
  public ItemWriter<NewsForUser> sendNews() {
    return chunk -> {
      if (chunk.getItems().isEmpty()) {
        return;
      }
      var news = (List<NewsForUser>) chunk.getItems();
      slackService.postStockInformation(news);
    };
  }
}
