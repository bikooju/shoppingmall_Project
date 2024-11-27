package project.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shoppingmall.entity.Member;
import project.shoppingmall.repository.MemberRepository;

@Service
@Transactional //로직을 처리하다가 에러가 발생한다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜준다.
@RequiredArgsConstructor
public class MemberService implements UserDetailsService { //UserDetailsService : 데이터베이스에서 회원정보를 가져오는 역할(인터페이스)
    //스프링 시큐리티에서 UserDetailsService를 구현하고 있는 클래스(MemberService)를 통해 로그인 기능을 구현한다

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //회원 중복 검사
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환


        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        //UserDetail를 구현하고 있는 User객체를 반환하기 위해서 User 객체를 생성(빌더패턴)
        return User.builder() //스프링 시큐리티에서 회원의 정보를 담기 위해서 사용하는 인터페이스는 UserDetails인데 이 인터페이스를 직접 구현하거나 스프링 시큐리티에서 제공하는 User 클래스 사용
                 .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString()) //enum 타입 role -> 문자열
                .build();

    }
}
