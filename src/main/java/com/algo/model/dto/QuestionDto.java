package com.algo.model.dto;

import com.algo.model.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * @author : iyeong-gyo
 * @package : com.algo.model.dto
 * @since : 20.11.23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionDto {

  private String title;
  private String url;
  private String fromSource;
  private Integer reviewCount;
}