package com.ll.sbb_1.Comment;

import java.time.LocalDateTime;

import com.ll.sbb_1.Answer.Answer;
import com.ll.sbb_1.Question.Question;
import com.ll.sbb_1.user.SiteUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser author;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Answer answer;

    public Integer getQuestionId() {
        Integer result = null;
        if (this.question != null) {
            result = this.question.getId();
        } else if (this.answer != null) {
            result = this.answer.getQuestion().getId();
        }
        return result;
    }

}