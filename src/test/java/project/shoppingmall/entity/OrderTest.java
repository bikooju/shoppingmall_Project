package project.shoppingmall.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import project.shoppingmall.enums.ItemSellStatus;
import project.shoppingmall.repository.ItemRepository;
import project.shoppingmall.repository.MemberRepository;
import project.shoppingmall.repository.OrderItemRepository;
import project.shoppingmall.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    //고객이 주문할 상품을 선택하고 주문할 때 주문 엔티티(부모)를 저장하면서 주문 상품 엔티티(자식)도 함께 저장되는 경우
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0; i<3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); //아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order 엔티티에 담아줍니다.
        }

        orderRepository.saveAndFlush(order); //order 엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영합니다.
        em.clear(); //영속성 컨텍스트의 상태를 초기화

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3,savedOrder.getOrderItems().size()); //OrderItem 엔티티 3개가 실제로 데이터베이스에 저장되었는지 검사
    }

    public Order createOrder() {
        Order order = new Order();

        for(int i=0; i<3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); ///아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order 엔티티에 담아줍니다.
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    //주문 엔티티(부모 엔티티)에서 주문 상품(자식 엔티티)를 삭제했을 때 orderItem(주문 상품)엔티티가 삭제되는지 테스트
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = createOrder();
        order.getOrderItems().remove(0); //order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 제거 [부모 엔티티와 연관관계 끊기]
        em.flush(); //flush()를 호출하면 orderItem를 삭제하는 쿼리문이 출력됨
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {

    }




}