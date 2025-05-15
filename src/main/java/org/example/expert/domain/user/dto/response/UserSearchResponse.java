package org.example.expert.domain.user.dto.response;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserSearchResponse {
	private final Long id;
	private final String email;
	private final String nickname;

	public UserSearchResponse(Long id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}
}
