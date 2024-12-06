package project.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.shoppingmall.dto.MemberFormDto;
import project.shoppingmall.enums.Role;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString
public class Member extends BaseEntity { //회원 엔티티 저장 시 자동으로 등록자, 수정자, 등록시간, 수정시간이 저장

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) //동일한 값이 데이터베이스에 들어올 수 없도록 unique 속성
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    //User 계정 생성
//    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
//        Member member = new Member();
//        member.setName(memberFormDto.getName());
//        member.setEmail(memberFormDto.getEmail());
//        member.setAddress(memberFormDto.getAddress());
//        String password = passwordEncoder.encode(memberFormDto.getPassword());
//        member.setPassword(password);
//        member.setRole(Role.USER);
//        return member;
//    }

    //ADMin Role 계정 생성
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}
