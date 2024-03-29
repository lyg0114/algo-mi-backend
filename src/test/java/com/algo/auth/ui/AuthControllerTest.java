package com.algo.auth.ui;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.algo.auth.domain.UserInfo;
import com.algo.auth.domain.UserInfoRepository;
import com.algo.auth.infrastructure.JwtUtil;
import com.algo.question.domain.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : iyeong-gyo
 * @package : com.algo.auth.ui
 * @since : 06.01.24
 */
@ActiveProfiles("test")
@DisplayName("로그인 인증 관련 테스트")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired protected QuestionRepository questionRepository;
  @Autowired protected UserInfoRepository userInfoRepository;
  @Autowired protected PasswordEncoder encoder;
  @Autowired protected JwtUtil jwtUtil;
  @Autowired protected ObjectMapper mapper;

  @BeforeEach
  void init() {
    userInfoRepository.save(UserInfo
        .builder()
        .userId(1L)
        .userName("kyle")
        .email("user@example.com")
        .passwd(encoder.encode("password"))
        .role("USER").isActivate(true).build());
  }

  @DisplayName("로그인 성공")
  @Test
  public void testLoginSuccess() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/api/rest/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\": \"user@example.com\", \"password\": \"password\"}"))
        .andExpect(status().isOk())
        .andReturn();
    Map result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
    Claims claims = jwtUtil.parseJwtClaims((String) result.get("token"));
    List roles = (List) claims.get("roles");
    assertThat((String) roles.get(0)).isEqualTo("USER");
    assertThat((String) claims.get("email")).isEqualTo("user@example.com");
    assertThat((String) claims.get("name")).isEqualTo("kyle");
  }


  @DisplayName("회원가입 성공")
  @Test
  public void testSignUpSuccess() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/api/rest/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\": \"newUser@example.com\","
                + " \"userName\": \"newUserName\","
                + " \"password\": \"password\""
                + "}"))
        .andExpect(status().isOk())
        .andReturn();
    Map result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
    assertThat((String) result.get("email")).isEqualTo("newUser@example.com");
  }

  //TODO : E-mail 체크 성공 테스트코드 작성
  @DisplayName("E-mail 체크 성공")
  @Test
  public void testEmailCheckSuccess() throws Exception {
  }
}