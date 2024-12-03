package project.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; //한 번의 주문으로 여러개의 주문상품 주문할 수 있음

    @ManyToOne
    @JoinColumn(name = "item_id") //하나의 상품은 여러 주문 상품으로 들어갈 수 있음
    private Item item;

    private int orderPrice; //주문 가격

    private int count; //수량

    private LocalDateTime regTime;

    private LocalDateTime updateTime;


}
