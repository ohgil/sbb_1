package com.ll.sbb_1.user;

import com.ll.sbb_1.Answer.AnswerService;
import com.ll.sbb_1.Comment.CommentService;
import com.ll.sbb_1.DataNotFoundException;
import com.ll.sbb_1.Question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private final UserEmailService userEmailService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorret",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";

        }

        try {
            userService.create(
                    userCreateForm.getUsername(),
                    userCreateForm.getEmail(),
                    userCreateForm.getPassword1(),
                    userCreateForm.getNickname());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("sigupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @PostMapping("/signup/emailConfirm")
    @ResponseBody
    public String emailConfirm(@RequestParam("email") String email) {
        String genCode = this.userService.genConfirmCode(8);
        System.out.println(genCode);
        this.userEmailService.mailSend(email, "이메일 인증", genCode);
        return this.userService.getEmailConfirmCode(genCode);
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        model.addAttribute("questionList",
                questionService.getCurrentListByUser(username, 5));
        model.addAttribute("answerList",
                answerService.getCurrentListByUser(username, 5));
        model.addAttribute("commentList",
                commentService.getCurrentListByUser(username, 5));
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/password")
    public String modifyPassword(UserPasswordForm userPasswordForm) {
        return "modify_password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/password")
    public String modifyPassword(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "modify_password";
        }

        SiteUser user = this.userService.getUser(principal.getName());
        if (!this.userService.confirmPassword(userPasswordForm.getPresentPW(), user)) {
            bindingResult.rejectValue("presentPW", "passwordInCorrect",
                    "현재 비밀번호를 바르게 입력해주세요.");
            return "modify_password_form";
        }

        // 비밀번호와 비밀번호 확인에 입력한 문자열이 서로 다르면 다시 입력 하도록
        if (!userPasswordForm.getNewPW().equals(userPasswordForm.getNewPW2())) {
            bindingResult.rejectValue("newPW2", "passwordInCorrect",
                    "입력한 비밀번호가 일치하지 않습니다.");
            return "modify_password_form";
        }

        userService.modifyPassword(userPasswordForm.getNewPW(), user);

        return "redirect:/user/logout";
    }

    @GetMapping("/find/username")
    public String findUsername() {
        return "find_username";
    }

    @PostMapping("/find/username")
    @ResponseBody
    public String findUsername(@RequestParam("email") String email) {
        return this.userService.getUserByEmail(email).getUsername();
    }

    @GetMapping("/find/password")
    public String findPassword() {
        return "find_password";
    }

    @PostMapping("/find/password")
    @ResponseBody
    public String findPassword(@RequestParam("username") String username, @RequestParam("email") String email) {
        SiteUser user = this.userService.getUserByUsernameAndEmail(username, email);

        if (user == null) {
            throw new DataNotFoundException("siteuser not found");
        }

        String genPassword = this.userService.getTemporalPassword(16);
        System.out.println(genPassword);
        this.userEmailService.mailSend(email, "비밀번호 찾기", genPassword);
        this.userService.modifyPassword(genPassword, user);

        return "";
    }
}
