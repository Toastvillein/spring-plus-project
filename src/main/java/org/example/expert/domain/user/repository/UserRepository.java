package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // 레벨 13 쿼리 DSL의 성능이 더 좋다하여 삭제한 JPQL
    // @Query("SELECT u FROM User u WHERE u.nickname = :nickname ORDER BY u.id")
    // Slice<UserSearchResponse> findByNickname(@Param("nickname")String nickname, Pageable pageable);
}
