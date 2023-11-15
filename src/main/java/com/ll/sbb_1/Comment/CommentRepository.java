package com.ll.sbb_1.Comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c "
            + "from Comment c "
            + "join SiteUser u on c.author=u "
            + "where u.username = :username "
            + "order by c.createDate desc ")
    List<Comment> findCurrentComment(@Param("username") String username, Pageable pageable);
}