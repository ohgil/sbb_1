package com.ll.sbb_1.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 25)
    @NotEmpty
    private String username;

    private String password1;
    private String password2;
    @NotEmpty
    private String nickname;
    @Email
    @NotEmpty
    private String email;
}
