package project.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import project.shoppingmall.enums.ItemSellStatus;

@Getter @Setter
public class ItemSearchDto { //상품 데이터 조회 시 상품 조회 조건을 가지고 있는 ItemSearchDto 클래스

    private String searchDateType;

    /*
    현재 시간과 상품 등록일 비교해서 상품 데이터 조회
    all : 상품 등록일 전체
    1d : 최근 하루 동안 등록된 상품
    1w : 최근 일주일 동안 등록된 상품
    1m : 최근 한달 동안 등록된 상품
    6m : 최근 6개월 동안 등록된 상품
     */

    private ItemSellStatus searchSellStatus;

    private String searchBy;
    /*
    itemName : 상품몀
    createdBy : 상품 등록자 아이디
     */

    private String searchQuery = "";
    /*
    조회할 검색어 저장할 변수입니다. searchBy가 itemName 일 경우 상품명을 기준으로 검색하고, createdBy일 경우 상품 등록자 아이디 기준으로 검색합니다.
     */
}
