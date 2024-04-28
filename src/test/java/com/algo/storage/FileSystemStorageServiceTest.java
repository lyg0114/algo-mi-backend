package com.algo.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

/**
 * @author : iyeong-gyo
 * @package : com.algo.storage
 * @since : 26.04.24
 */
class FileSystemStorageServiceTest {

  private StorageProperties properties = new StorageProperties();
  private FileSystemStorageService service;

  @BeforeEach
  public void init() {
    properties.setLocation("build/files/" + Math.abs(new Random().nextLong()));
    service = new FileSystemStorageService(properties);
    service.init();
  }

  @DisplayName("파일 저장 테스트")
  @Test
  public void saveAndLoad() {
    service.store(
        new MockMultipartFile("foo",
            "foo.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, Algo".getBytes())
    );

    assertThat(service.load("foo.txt")).exists();
  }
}