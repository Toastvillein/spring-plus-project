package org.example.expert.config.jwt;

import org.example.expert.domain.user.enums.UserRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserAuth {
	private final Long id;
	private final String email;
	private final UserRole userRole;
	private final String nickname;
}
