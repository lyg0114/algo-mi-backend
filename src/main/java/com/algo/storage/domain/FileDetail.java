package com.algo.storage.domain;

import com.algo.auth.domain.UserInfo;
import com.algo.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.algo.storage.domain
 * @since : 28.04.24
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_detail")
public class FileDetail extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long fileId;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "file_url")
  private String fileUri;

  @Column(name = "file_download_url")
  private String fileDownloadUri;

  @Column(name = "file_size")
  private long fileSize;

  @OneToOne
  @JoinColumn(name = "uesr_id")
  private UserInfo fileUploader;
}