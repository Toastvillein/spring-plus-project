package org.example.expert.domain.user.repository;

import java.util.List;

import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
	/* 쿼리 DSL의 Projections 활용이 필요한 필드만 추출해 바로 DTO로 반환하기 때문에
	*  성능이 좋다하여 기존 JPQL에서 변경
	* */
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<UserSearchResponse> findByNickname(String nickname, Pageable pageable) {
		QUser user = QUser.user;

		List<UserSearchResponse> content = queryFactory
			.select(Projections.constructor(
				UserSearchResponse.class,
				user.id,
				user.email,
				user.nickname
			))
			.from(user)
			.where(user.nickname.eq(nickname))
			.orderBy(user.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1 ) // Slice는 기본 size의 +1까지 추출하여 확인해서 다음 페이지가 있는지만 파악함
			.fetch();

		boolean hasNext = content.size() > pageable.getPageSize();

		// 다음 페이지가 존재하는지 확인후 삭제, 인덱스 오류도 방지함
		if(hasNext){
			content.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(content,pageable,hasNext);
	}

	// 레벨 13 비교용 쿼리 DSL
	@Override
	public Page<UserSearchResponse> findPageByNickname(String nickname, Pageable pageable) {
		QUser user = QUser.user;

		List<UserSearchResponse> content = queryFactory
			.select(Projections.constructor(
				UserSearchResponse.class,
				user.id,
				user.email,
				user.nickname
			))
			.from(user)
			.where(user.nickname.eq(nickname))
			.orderBy(user.id.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(user.id)
			.from(user)
			.where(user.nickname.eq(nickname))
			.fetch()
			.size();


		return new PageImpl<>(content,pageable,total);
	}
}
