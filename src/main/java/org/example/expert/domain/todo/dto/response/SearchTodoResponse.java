package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SearchTodoResponse {
	// 본래 record를 사용했지만 Projections가 생성자 주입 방식이라 new를 못쓰는 record 대신 class로 변경
	private final String title;
	private final int managerCount;
	private final int commentCount;

	public SearchTodoResponse(String title, int managerCount, int commentCount) {
		this.title = title;
		this.managerCount = managerCount;
		this.commentCount = commentCount;
	}
}
