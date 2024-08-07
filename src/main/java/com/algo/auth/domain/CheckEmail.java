package com.algo.auth.domain;

import java.time.LocalDateTime;

import com.algo.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.algo.auth.domain
 * @since : 26.03.24
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "check_email")
public class CheckEmail extends BaseEntity {

	@Id
	@Column(name = "check_id", nullable = false)
	private String checkId;

	@Column(name = "validate_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime validateDate;

	@Column(name = "is_expire")
	private Boolean isExpire;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserInfo userInfo;

	public void expire() {
		this.isExpire = true;
	}
}
