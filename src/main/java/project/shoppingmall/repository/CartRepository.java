package project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shoppingmall.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
