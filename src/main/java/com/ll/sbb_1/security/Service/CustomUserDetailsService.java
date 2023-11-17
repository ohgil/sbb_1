package com.ll.sbb_1.security.Service;

import com.ll.sbb_1.user.SiteUser;
import com.ll.sbb_1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _user =  userRepository.findByusername(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }
        SiteUser user = _user.get();

        List< GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("user"));

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
