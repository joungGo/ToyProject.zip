package org.example.exercisespringboot.user;

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

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService { // 스프링 시큐리티가 로그인 시 사용할 UserSecurityService 는 스프링 시큐리티가 제공하는 UserDetailsService 인터페이스를 구현(implements)해야 한다.

    private final UserRepository userRepository;

    @Override
    // loadUserByUsername 메서드는 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
        loadUserByUsername 메서드는 사용자명(username)으로 사용자 정보를 조회하여 UserDetails 인터페이스를 구현한 객체를 리턴
        UserDetails 란 사용자 정보를 나타내는 인터페이스로 사용자명, 패스워드, 권한 등을 포함.
        User 객체와 UserDetailsService 는 UserDetails 인터페이스를 구현한 클래스
        */
        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username); // 사용자명(username)으로 사용자 정보를 조회, 사용자 정보에는 사용자명, 패스워드, 이메일 등이 포함
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get(); // 사용자 정보를 조회하여 SiteUser 객체로 변환, 이유는
        List<GrantedAuthority> authorities = new ArrayList<>();
        /*
        GrantedAuthority 란 사용자의 권한을 나타내는 인터페이스
        SimpleGrantedAuthority 는 GrantedAuthority 인터페이스를 구현한 클래스
        리스트로 사용한 이유는 사용자의 권한이 여러 개일 수 있기 때문
        */
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue())); // UserRole.ADMIN.getValue() 는 "ROLE_ADMIN" 을 리턴
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue())); // UserRole.ADMIN.getValue() 는 "ROLE_USER" 을 리턴
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities); // User 클래스는 UserDetails 인터페이스를 구현한 클래스, User 클래스의 생성자는 사용자명, 패스워드, 권한을 매개변수로 받음
    }
}