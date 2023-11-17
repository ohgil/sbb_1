package com.ll.sbb_1.user;

import com.ll.sbb_1.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password, String nickname){
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        this.userRepository.save(user);
        return user;
    }

//    public SiteUser join(String username, String password, String email, String nickname) {
//
//        SiteUser user = SiteUser.builder()
//                .username(username)
//                .password(passwordEncoder.encode(password))
//                .email(email)
//                .nickname(nickname)
//                .build();
//        userRepository.save(user);
//
//        return user;
//    }


    public String getEmailConfirmCode(String code) {
        return passwordEncoder.encode(code);
    }

    public String genConfirmCode(int length) {
        String candidateCode = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom secureRandom = new SecureRandom();

        String code = "";
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(candidateCode.length());
            code += candidateCode.charAt(index);
        }

        return code;
    }

    public String getTemporalPassword(int length) {
        String candidatePassword = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*";
        SecureRandom secureRandom = new SecureRandom();

        String password = "";
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(candidatePassword.length());
            password += candidatePassword.charAt(index);
        }

        return password;
    }


    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUser getUserByEmail(String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByemail(email);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public void modifyPassword(String password, SiteUser user) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }

    public boolean confirmPassword(String password, SiteUser user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public boolean confirmCertificationCode(String inputCode, String genCode) {
        return passwordEncoder.matches(inputCode, genCode);
    }

    public SiteUser getUserByUsernameAndEmail(String username, String email) {
        return this.userRepository.findByUsernameAndEmail(username, email);
    }
}
