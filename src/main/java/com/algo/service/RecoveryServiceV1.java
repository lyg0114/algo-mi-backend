package com.algo.service;

import com.algo.model.dto.QuestionDto;
import com.algo.model.entity.Question;
import com.algo.repository.querydsl.QuestionCustomRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author : iyeong-gyo
 * @package : com.algo.service
 * @since : 22.12.23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecoveryServiceV1 implements RecoveryService {

  @Value(value = "${slack.webhook.url}")
  private String slackWebHookUrl;
  private final QuestionCustomRepository questionCustomRepository;
  private final RestTemplate restTemplate;

  @Override
  @Scheduled(cron = "0 0 13 * * ?")
  public void getRecoveryTargetsAndNoteToUser() {
    List<Question> targets = getRecoveryTarges();
    String messageJson = createMessageJson(targets);
    postInquiry(messageJson);
  }

  private List<Question> getRecoveryTarges() {
    return questionCustomRepository
        .findQuestions(
            QuestionDto.builder()
                .fromDt(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.MIDNIGHT))
                .toDt(LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.MAX))
                .build()
        )
        .fetch();
  }

  private String createMessageJson(List<Question> targets) {
    StringBuffer sb = new StringBuffer();
    if (!targets.isEmpty()) {
      sb.append("######################\n");
      sb.append("## 오늘 복습할 문제 ##\n");
      sb.append("######################\n");
      for (Question target : targets) {
        sb.append(target.getTitle())
            .append(" : ")
            .append(target.getUrl())
            .append("\n");
      }
    } else {
      sb.append("##############\n");
      sb.append("## 분발하자 ##\n");
      sb.append("##############\n");
    }
    return "{\"text\":\"" + sb + "\"}";
  }

  public void postInquiry(String messageJson) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> requestEntity = new HttpEntity<>(messageJson, headers);
    restTemplate.postForEntity(slackWebHookUrl, requestEntity, String.class);
  }
}