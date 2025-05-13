package org.example.expert.domain.todo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
	// DSL 문법자체가 어렵다기보다 초기 설정(build.gradle/Config)이 제일 어려웠다.
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		Todo result = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user,user)
			.fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public Page<SearchTodoResponse> findAllByTitleOrDateOrNickname(String title, String nickname, LocalDateTime startDate,
		LocalDateTime endDate, Pageable pageable) {
		QTodo todo = QTodo.todo;
		/* DTO로 바로 반환해야하니 Projections 사용 -> 반환타입도 바로 DTO로 가게끔 변경
		* 프로젝션은 엔티티 전체가 아닌 필요한 필드만 조회해서 성능 최적화를 할 수 있음
		* */
		List<SearchTodoResponse> content = queryFactory
			.select(Projections.constructor(
				SearchTodoResponse.class,
				todo.title,
				todo.managers.size(),
				todo.comments.size()
			))
			.from(todo)
			.where(
				/* WHERE (:title IS NULL OR title = :title)을 쿼리 DSL 형식으로 바꾼것
				* 삼항 연산자를 활용하고 contains(LIKE)를 사용해 일부분만 일치해도 결과가 나오게끔 함
				* */
				title != null ? todo.title.contains(title) : null,
				nickname != null ? todo.user.nickname.contains(nickname) : null,
				startDate != null ? todo.createdAt.goe(startDate) : null,
				endDate != null ? todo.createdAt.loe(endDate) : null
			)
			.orderBy(todo.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 쿼리 DSL에서 페이징 처리를 위한 총 데이터 수를 계산하기 위한 메서드
		long total = queryFactory
			.select(todo.id)
			.from(todo)
			.where(
				title != null ? todo.title.contains(title) : null,
				nickname != null ? todo.user.nickname.contains(nickname) : null,
				startDate != null ? todo.createdAt.goe(startDate) : null,
				endDate != null ? todo.createdAt.loe(endDate) : null
			)
			.fetch()
			.size();

		return new PageImpl<>(content,pageable,total);
	}
}
