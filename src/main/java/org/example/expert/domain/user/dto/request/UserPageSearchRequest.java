package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPageSearchRequest {
	private String nickname;
	@Min(1)
	private int page = 1;
	@Min(10)
	private int size = 10;
}
