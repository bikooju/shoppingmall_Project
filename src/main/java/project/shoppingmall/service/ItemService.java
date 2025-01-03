package project.shoppingmall.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.shoppingmall.dto.ItemFormDto;
import project.shoppingmall.dto.ItemImgDto;
import project.shoppingmall.dto.ItemSearchDto;
import project.shoppingmall.dto.MainItemDto;
import project.shoppingmall.entity.Item;
import project.shoppingmall.entity.ItemImg;
import project.shoppingmall.repository.ItemImgRepository;
import project.shoppingmall.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem(); //상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성합니다.
        itemRepository.save(item); //상품 데이터를 저장합니다.

        //이미지 등록
        for(int i=0; i<itemImgFileList.size();i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0) { //첫 번째 이미지일 경우 상품 이미지 여부 값을 "Y"로 세팅합니다.
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //상품의 이미지 정보를 저장합니다.
        }

        return item.getId();
    }

    //readOnly = true로 설정된 트랜잭션에서는 데이터 수정(INSERT, UPDATE, DELETE) 작업이 허용되지 않습니다.
    //데이터베이스에서 읽기 작업(SELECT)을 최적화하도록 동작합니다.
    @Transactional(readOnly = true) //JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    public ItemFormDto getItemDetail(Long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList) { //조회한 itemImg엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()) //상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 이미지를 조회합니다.
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); //상품 등록 화면으로부터 전달 받은 ItemformDto를 통해 상품 엔티티를 업데이트합니다.

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회합니다.

        //이미지 등록
        for(int i=0;i< itemImgFileList.size();i++) {
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
            //상품 이미지를 업로드하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일정보를 파라미터로 전달합니다.
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }
}
