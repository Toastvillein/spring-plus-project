package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchTodoRequest {
	// 본래 record를 사용했지만 default값 지정이 안되서 class로 변경
	private String title;
	private String nickname;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	// 만에하나 0이하가 반환될시 IndexOutOfBoundsException이 터질 수 있어서 Validation으로 미연에 방지
	@Min(1)
	private int page = 1;
	@Min(10)
	private int size = 10;
}
