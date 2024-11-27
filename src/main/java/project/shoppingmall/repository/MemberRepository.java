package project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shoppingmall.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> { //Member 엔티티를 데이터베이스에 저장할 수 있도록 MemberRepository 만들기

    Member findByEmail(String email); //회원 가입 시 중복된 회원이 있는지 검사하기 위해서 이메일로 회원 검사
}
