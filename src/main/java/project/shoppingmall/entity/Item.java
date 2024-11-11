package project.shoppingmall.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.shoppingmall.entity.enums.ItemSellStatus;

@Getter
@Setter
@ToString
public class Item {
    private Long id;

    private String itemName;

    private int price;

    private int stockNumber; //재고수량

    private String itemDetail; //상품 상세 설명

    private ItemSellStatus itemSellStatus; //상품 판매 상태


}
