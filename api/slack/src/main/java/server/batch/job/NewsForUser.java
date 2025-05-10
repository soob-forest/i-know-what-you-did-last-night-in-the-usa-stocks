package server.batch.job;

import api.subscriptions.domain.News.NewsDto;
import java.util.List;


public record NewsForUser(String userId, List<NewsDto> news) {

//  private String userId;
//  private List<NewsDto> news;


}
