package com.algo.controller.rest;

import com.algo.model.dto.QuestionDto;
import com.algo.model.entity.Question;
import com.algo.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : iyeong-gyo
 * @package : com.algo.controller.rest
 * @since : 01.12.23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/question")
public class QuestionRestController {

  private final QuestionService questionService;
  private final ModelMapper modelMapper;

  @GetMapping("/{questionId}")
  @PreAuthorize("hasRole(@roles.USER)")
  public ResponseEntity<QuestionDto> getQuestion(long questionId) {
    Question question = questionService.findQuestionById(questionId);
    if (question == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(
        question.converToDto(modelMapper), HttpStatus.OK)
        ;
  }

  @GetMapping
  @PreAuthorize("hasRole(@roles.USER)")
  public ResponseEntity<List<QuestionDto>> listQuestion(QuestionDto questionDto) {
    return null;
  }

  @PostMapping
  @PreAuthorize("hasRole(@roles.USER)")
  public ResponseEntity<QuestionDto> addQuestion(QuestionDto questionDto) {
    return null;
  }

  @PutMapping("/{questionId}")
  @PreAuthorize("hasRole(@roles.USER)")
  public ResponseEntity<Question> updatePet(long questionId, QuestionDto questionDto) {
    return null;
  }

  @DeleteMapping("/{questionId}")
  @PreAuthorize("hasRole(@roles.USER)")
  public ResponseEntity<Question> deletePet(long questionId) {
    return null;
  }
}