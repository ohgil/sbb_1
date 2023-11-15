package com.ll.sbb_1.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ll.sbb_1.DataNotFoundException;
import com.ll.sbb_1.Question.Question;
import com.ll.sbb_1.user.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Question question, String content, SiteUser author) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setQuestion(question);
        c.setAuthor(author);
        c = this.commentRepository.save(c);
        return c;
    }

    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("코멘트를 찾을 수 없습니다.");
        }
        // return this.commentRepository.findById(id);
    }

    public Comment modify(Comment cmt, String content) {
        cmt.setContent(content);
        cmt.setModifyDate(LocalDateTime.now());
        cmt = this.commentRepository.save(cmt);
        return cmt;
    }

    public void delete(Comment c) {
        this.commentRepository.delete(c);
    }

    public List<Comment> getCurrentListByUser(String username, int num) {
        Pageable pageable = PageRequest.of(0, num);
        return commentRepository.findCurrentComment(username, pageable);
    }
}
