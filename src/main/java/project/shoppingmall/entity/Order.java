package project.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.shoppingmall.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //CascadeType.ALL => 부모(One => Order) 엔티티의 영속성 상태 변화를 자식(Many => OrderItem) 엔티티에 모두 전이
    private List<OrderItem> orderItems = new ArrayList<>();
    //왜래키(order_id)가 oder_item 테이블에 있으므로 연관 관계의 주인은 OrderItem 엔티티입니다.
    //Order 엔티티가 주인이 아니므로 "mappedBy" 속성으로 연관 관계의 주인을 설정합니다.
    //속성의 값으로 "order" => OrderItem에 있는 Order에 의해 관리된다.
    //기본적으로 연관 관계 단방향 매핑으로 설계 후 나중에 필요할 경우 양방향 매핑을 추가하기


    private LocalDateTime regTime; //등록 시간

    private LocalDateTime updateTime; //수정 시간



}
