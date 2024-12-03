package project.shoppingmall.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import project.shoppingmall.dto.MemberFormDto;
import project.shoppingmall.repository.CartRepository;
import project.shoppingmall.repository.MemberRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트 중에 발생하는 예외가 트랜잭션을 롤백하게 하여, 테스트가 실패하더라도 데이터베이스의 상태가 깨지지 않도록 합니다.
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("주현준");
        memberFormDto.setAddress("경기도 부천시 상동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); //JPA는 영속성 컨테스트에 데이터를 저장 후 트랜잭션이 끝날 때 강제로 flush()를 호출하여 데이터베이스에 반영합니다.
        em.clear(); //JPA는 영속성 컨테스트로부터 엔티티를 조회한 후 영속성 컨테스트에 엔티티가 없을 경우 데이터베이스를 조회합니다.
        //실제 데이터베이스에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고오는지 보기 위해서 영속성 컨테스트를 비워주겠습니다.

        Cart savedCart = cartRepository.findById(cart.getId()) //저장된 장바구니 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId()); //장바구니에서 조회한 회원 ID == 회원 엔티티에서 조회한 ID

    }
}