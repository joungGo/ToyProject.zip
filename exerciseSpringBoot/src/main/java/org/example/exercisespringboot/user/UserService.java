package org.example.exercisespringboot.user;

import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.exception.DataNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //  비크립트(BCrypt) 해시 함수를 사용하는데, 비크립트는 해시 함수의 하나로 주로 비밀번호와 같은 보안 정보를 안전하게 저장하고 검증할 때 사용하는 암호화 기술
        //user.setPassword(passwordEncoder.encode(password));
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteUser not found");
        }
    }
}