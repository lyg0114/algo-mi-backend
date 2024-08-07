package com.algo.question.ui;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.algo.auth.infrastructure.AuthenticationUtil;
import com.algo.common.dto.UserInfoRequest;
import com.algo.question.application.QuestionService;
import com.algo.question.domain.Question;
import com.algo.question.dto.QuestionRequest;
import com.algo.question.dto.QuestionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : iyeong-gyo
 * @package : com.algo.controller.rest
 * @since : 01.12.23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionRestController {

	private final QuestionService questionService;
	private final ModelMapper modelMapper;
	private final AuthenticationUtil authenticationUtil;

	@GetMapping
	public ResponseEntity<Page<QuestionResponse>> getQuestions(
		QuestionRequest questionRequest, @PageableDefault(size = 12) Pageable pageable
	) {
		questionRequest.setEmail(authenticationUtil.getEmail());
		Page<QuestionResponse> questions = questionService.findPaginatedForQuestions(questionRequest, pageable);
		if (questions != null && !questions.isEmpty()) {
			return new ResponseEntity<>(questions, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/{questionId}")
	public ResponseEntity<QuestionResponse> getQuestion(@PathVariable long questionId) {
		Question question = questionService.findQuestionById(questionId);
		if (question != null) {
			return new ResponseEntity<>(
				question.converToDto(modelMapper), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<QuestionResponse> addQuestion(
		@RequestBody QuestionRequest questionRequest) {
		HttpHeaders headers = new HttpHeaders();
		questionRequest.setEmail(authenticationUtil.getEmail());
		QuestionResponse addQuestionResponse = questionService.addQuestion(questionRequest);
		headers.setLocation(UriComponentsBuilder
			.newInstance()
			.path("/question/{id}")
			.buildAndExpand(addQuestionResponse.getId())
			.toUri());
		return new ResponseEntity<>(addQuestionResponse, headers, HttpStatus.CREATED);
	}

	@PutMapping("/{questionId}")
	public ResponseEntity<QuestionResponse> updateQuestion(
		@PathVariable long questionId, @RequestBody QuestionRequest questionRequest
	) {
		questionRequest.setEmail(authenticationUtil.getEmail());
		QuestionResponse updateQuestionResponse = questionService.updateQuestion(questionId, questionRequest);
		if (updateQuestionResponse == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updateQuestionResponse, HttpStatus.CREATED);
	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity<QuestionResponse> deleteQuestion(@PathVariable long questionId) {
		questionService.deleteQuestion(questionId, new UserInfoRequest(authenticationUtil.getEmail()));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
