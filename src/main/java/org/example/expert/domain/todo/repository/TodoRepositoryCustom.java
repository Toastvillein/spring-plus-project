package org.example.expert.domain.todo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface TodoRepositoryCustom {
	/* TodoRepository의 메서드 중 하나만 쿼리 DSL로 변경해야하니 따로 인터페이스를 만들어주고
	*  변경할 JPA쿼리만 추가
	*  또한 이후 TodoRepository에 해당 인터페이스의 구현체(TodoRepositoryImpl)를 자동으로
	*  인식하게 만들기 위해 명시적으로 상속해줌
	* */
	Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

	// 레벨 10 검색기능 Repository 메서드
	Page<SearchTodoResponse> findAllByTitleOrDateOrNickname(
		@Param("title") String title,
		@Param("nickname") String nickname,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate,
		Pageable pageable);
}
