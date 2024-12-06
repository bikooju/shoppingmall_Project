package project.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@Table(name = "cart")
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) //기본값이 즉시로딩(fetch = FetchType.EAGER) => 해당 엔티티와 매핑된 엔티티도 한번에 조회
    @JoinColumn(name = "member_id") //외래키 지정
    private Member member;
}
