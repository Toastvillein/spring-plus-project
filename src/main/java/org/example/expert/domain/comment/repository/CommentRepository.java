package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /* 필수레벨 7 N+1 문제 FETCH JOIN 사용
    *  페이징 처리를 해야할 시엔 @EntityGraph 사용
    *  @EntityGraph(attributePaths = "user")
    *  @Query("SELECT c FROM Comment c WHERE c.todo.id = :todoId")
    *  Page<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId, Pageable pageable);
    * */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.todo.id = :todoId")
    List<Comment> findByTodoIdWithUser(@Param("todoId") Long todoId);
}
