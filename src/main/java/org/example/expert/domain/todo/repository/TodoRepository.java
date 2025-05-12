package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    //필수 레벨 3 weather 조건 추가(nullable), 수정일 기준 추가(nullable)
    @Query("SELECT t FROM Todo t "
        + "LEFT JOIN FETCH t.user u "
        + "WHERE (:weather IS NULL OR t.weather = :weather) "
        + "AND (:startDate IS NULL OR t.modifiedAt >= :startDate) "
        + "AND (:endDate IS NULL OR t.modifiedAt <= :endDate) "
        + "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(
        Pageable pageable,
        @Param("weather") String weather,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    // 필수레벨 8 쿼리 DSL로 변경하기
    // @Query("SELECT t FROM Todo t " +
    //         "LEFT JOIN t.user " +
    //         "WHERE t.id = :todoId")
    // Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
