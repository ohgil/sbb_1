package com.ll.sbb_1.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @NotEmpty
    private String username;

    private String password1;
    private String password2;
    private String nickname;
    @Email
    @NotEmpty
    private String email;
}
