package com.algo.alert.application;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.algo.auth.domain.CheckEmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : iyeong-gyo
 * @package : com.algo.alert.application
 * @since : 27.03.24
 */
@Profile("test")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceTest implements EmailService {

	@Override
	public void sendSimpleMessage() {
		log.info("###########################");
		log.info("call sendSimpleMessage success");
		log.info("###########################");
	}

	@Override
	public void sendSignUpEamil(CheckEmail checkEmail) {
		log.info("###########################");
		log.info("call sendSignUpEamil success");
		log.info("###########################");
	}
}
