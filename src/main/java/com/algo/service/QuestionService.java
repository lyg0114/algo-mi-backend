package com.algo.service;

import com.algo.config.security.AuthenticationUtil;
import com.algo.model.dto.QuestionDto;
import com.algo.model.entity.Question;
import com.algo.model.entity.UserInfo;
import com.algo.repository.QuestionRepository;
import com.algo.repository.UserInfoRepository;
import com.algo.repository.querydsl.QuestionCustomRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : iyeong-gyo
 * @package : com.algo.service
 * @since : 18.11.23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionService {

  private final QuestionCustomRepository questionCustomRepository;
  private final QuestionRepository questionRepository;
  private final UserInfoRepository userInfoRepository;
  private final ModelMapper modelMapper;


  @Transactional(readOnly = true)
  public Page<QuestionDto> findPaginatedForQuestions(QuestionDto questionDto, Pageable pageable) {
    return questionCustomRepository.findPaginatedForQuestions(questionDto, pageable);
  }

  @Transactional(readOnly = true)
  public Question findQuestionById(long questionId) {
    Question Queston = null;
    try {
      Queston = questionRepository.findById(questionId).orElseThrow();
    } catch (NoSuchElementException e) {
      return null;
    }
    return Queston;
  }

  @Transactional
  public QuestionDto addQuestion(QuestionDto addQuestionDto) {
    UserInfo userInfo = userInfoRepository.findUserInfoByEmail(
        AuthenticationUtil.getUserName()
    );
    Question addQuestion = addQuestionDto.converTnEntity(modelMapper);
    addQuestion.setUserInfo(userInfo);
    return questionRepository
        .save(addQuestion)
        .converToDto(modelMapper)
        ;
  }

  @Transactional
  public QuestionDto updateQuestion(long questionId, QuestionDto questionDto) {
    Question question = questionDto.converTnEntity(modelMapper);
    Question targetQuestion = null;
    try {
      targetQuestion = questionRepository.findById(questionId).orElseThrow();
      targetQuestion.update(question);
      return targetQuestion.converToDto(modelMapper);
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  @Transactional
  public void deleteQuestion(long questionId) {
    questionRepository.deleteById(questionId);
  }
}
