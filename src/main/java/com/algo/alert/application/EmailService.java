package com.algo.alert.application;

import org.springframework.scheduling.annotation.Scheduled;

import com.algo.auth.domain.CheckEmail;

/**
 * @author : iyeong-gyo
 * @package : com.algo.alert.application
 * @since : 27.03.24
 */
public interface EmailService {

	@Scheduled(cron = "${cron.rule}", zone = "${time.zone}")
	void sendSimpleMessage();

	void sendSignUpEamil(CheckEmail checkEmail);
}
