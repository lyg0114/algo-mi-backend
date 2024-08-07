package com.algo.recovery.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.algo.question.dto.QuestionResponse;
import com.algo.recovery.dto.RecoveryContents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : iyeong-gyo
 * @package : com.algo.service
 * @since : 22.12.23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecoveryService {

	public static final String CREATE_RECOVERY_TARGETS_BY_YESTER_DAY = "CreateRecoveryTargetsByYesterDay";
	private final WebApplicationContext context;

	private List<QuestionResponse> createRecoveryTargets() {
		CreateRecovery createRecovery = null;
		assert context != null;
		createRecovery = context.getBean(CREATE_RECOVERY_TARGETS_BY_YESTER_DAY, CreateRecovery.class);
		return createRecovery.createRecoveryTargets();
	}

	public String createTitle() {
		LocalDate now = LocalDate.now();
		return "[" + now + " : 복습 리스트 ]";
	}

	public List<RecoveryContents> createContents() {
		Map<String, List<QuestionResponse>> collect = createRecoveryTargets()
			.stream()
			.collect(Collectors.groupingBy(QuestionResponse::getEmail));

		List<RecoveryContents> recoveryContents = new ArrayList<>();
		for (String email : collect.keySet()) {
			StringBuffer sb = new StringBuffer();
			List<QuestionResponse> questionResponses = collect.get(email);
			if (!questionResponses.isEmpty()) {
				sb.append("\n######################\n");
				sb.append("## 오늘 복습할 문제 ##\n");
				sb.append("######################\n");
				for (QuestionResponse target : questionResponses) {
					sb.append(target.getTitle())
						.append(" : ")
						.append(target.getUrl())
						.append("\n");
				}
			} else {
				sb.append("##############\n");
				sb.append("## 분발하자 ##\n");
				sb.append("##############\n");
			}
			recoveryContents.add(new RecoveryContents(email, sb.toString()));
		}
		return recoveryContents;
	}
}
