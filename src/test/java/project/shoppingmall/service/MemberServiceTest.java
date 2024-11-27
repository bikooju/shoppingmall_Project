package project.shoppingmall.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import project.shoppingmall.dto.MemberFormDto;
import project.shoppingmall.entity.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional //테스트 실행 후 롤백 철가 됩니다. 이를 통해 같은 메소드를 반복적으로 테스트할 수 있다.
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("주현준");
        memberFormDto.setAddress("경기도 부천시 원미구");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberService.saveMember(member);

        //then
//        assertThat(member).isEqualTo(savedMember);
        assertEquals(member.getEmail(), savedMember.getEmail()); //기댓값, 실제값
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        //when
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> memberService.saveMember(member2));

        //then
        assertEquals("이미 가입된 회원입니다.", e.getMessage());


    }




}