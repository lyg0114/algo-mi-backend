package com.algo.question.application;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algo.auth.domain.UserInfo;
import com.algo.auth.domain.UserInfoRepository;
import com.algo.common.dto.UserInfoRequest;
import com.algo.question.domain.Question;
import com.algo.question.domain.QuestionCustomRepository;
import com.algo.question.domain.QuestionRepository;
import com.algo.question.dto.QuestionRequest;
import com.algo.question.dto.QuestionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public Page<QuestionResponse> findPaginatedForQuestions(
		QuestionRequest QuestionRequest, Pageable pageable
	) {
		return questionCustomRepository.findPaginatedForQuestions(QuestionRequest, pageable);
	}

	@Transactional(readOnly = true)
	public Question findQuestionById(long questionId) {
		return questionRepository.findById(questionId)
			.orElseThrow(() -> new NoSuchElementException("문제 정보를 찾을 수 없습니다."));
	}

	@Transactional
	public QuestionResponse addQuestion(QuestionRequest question) {
		UserInfo userInfo = userInfoRepository.findUserInfoByEmailAndIsActivateTrue(question.getEmail())
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));
		Question addQuestion = question.converTnEntity();

		addQuestion.setUserInfo(userInfo);
		return questionRepository
			.save(addQuestion)
			.converToDto(modelMapper)
			;
	}

	@Transactional
	public QuestionResponse updateQuestion(long questionId, QuestionRequest questionRequest) {
		Question question = questionRequest.converTnEntity();
		Question targetQuestion = null;
		targetQuestion = questionRepository.findById(questionId)
			.orElseThrow(() -> new NoSuchElementException("문제 정보를 찾을 수 없습니다."));
		if (Objects.isNull(targetQuestion.getUserInfo())) {
			throw new NoSuchElementException("존재하지 않는 사용자 입니다.");
		}
		if (!targetQuestion.getUserInfo().getEmail().equals(questionRequest.getEmail())) {
			throw new NoSuchElementException("존재하지 않는 사용자 입니다.");
		}

		targetQuestion.update(question);
		return targetQuestion.converToDto(modelMapper);
	}

	@Transactional
	public void deleteQuestion(long questionId, UserInfoRequest userInfoRequest) {
		Question targetQuestion = questionRepository.findById(questionId)
			.orElseThrow(() -> new NoSuchElementException("문제 정보를 찾을 수 없습니다."));
		if (Objects.isNull(targetQuestion.getUserInfo())) {
			throw new NoSuchElementException("존재하지 않는 사용자 입니다.");
		}
		if (!targetQuestion.getUserInfo().getEmail().equals(userInfoRequest.getEmail())) {
			throw new NoSuchElementException("존재하지 않는 사용자 입니다.");
		}

		questionRepository.deleteById(questionId);
	}
}
