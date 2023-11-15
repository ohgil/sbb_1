package com.ll.sbb_1.Comment;

import java.security.Principal;

import com.ll.sbb_1.Question.Question;
import com.ll.sbb_1.Question.QuestionService;
import com.ll.sbb_1.user.SiteUser;
import com.ll.sbb_1.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/question/{id}")
    public String createQuestionComment(CommentForm commentForm) {
        return "comment_form";
    }

    // 질문에 대한 코멘트
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/question/{id}")
    public String createQuestionComment(Model model, @PathVariable("id") Integer id, @Valid CommentForm commentForm,
                                        BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        SiteUser user = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        Comment comment = this.commentService.create(question, commentForm.getContent(), user);
        return String.format("redirect:/question/detail/%s#comment_%s", comment.getQuestion().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
                                @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/question/detail/%s", comment.getQuestion().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/question/detail/%s", comment.getQuestion().getId());
    }

}