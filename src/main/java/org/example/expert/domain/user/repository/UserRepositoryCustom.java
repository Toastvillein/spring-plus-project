package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryCustom {

	Slice<UserSearchResponse> findByNickname(@Param("nickname")String nickname, Pageable pageable);
	// 레벨 13 비교용 JPA 쿼리
	Page<UserSearchResponse> findPageByNickname(@Param("nickname")String nickname, Pageable pageable);
}
