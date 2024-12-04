package project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shoppingmall.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { //주문 데이터를 데이터베이스에 저장하고 저장한 주문 상품 데이터를 조회하기 위해 레포지토리 만듬
}
