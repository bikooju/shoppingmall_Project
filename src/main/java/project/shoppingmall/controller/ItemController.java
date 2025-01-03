package project.shoppingmall.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.shoppingmall.dto.ItemFormDto;
import project.shoppingmall.dto.ItemSearchDto;
import project.shoppingmall.entity.Item;
import project.shoppingmall.service.ItemService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) { //상품 등록 시 필수 값이 없다면(오류) 다시 상품 등록 페이지로 전환
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        //상품 저장 로직 호출
        try {
            itemService.saveItem(itemFormDto, itemImgFileList); //매개변수로 상품 정보(itemForDto)와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨줌
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등로 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; //상품이 정상적으로 등록되었다면 메인페이지로 이동
    }


    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto()); //ItemFormDto를 model 객체에 담아서 뷰로 전달
        return "item/itemForm";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto); //조회한 상품 데이터를 모델에 담아서 뷰로 전달
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지 정보는 필수 입력 값입니다.");
            return "item/itmeForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList); //상품 수정 로직 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달하는 로직
    @GetMapping({"/admin/items", "/admin/items/{page}"}) //상품 관리 화면 진입 시 URL에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매핑합니다.
    public String itemManage (ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); //페이징을 위해서 PageRequest.of 메소드를 통해 Pageable 객체를 생성합니다.
        //첫 번째 파라미터로는 조회할 페이지 번호, 두 번째 파라미터로는 한 번에 가지고 올 데이터 수를 넣어줍니다.
        //URL에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅하고, 페이지 번호가 없으면 0페이지를 조회하도록 합니다.
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); //조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 겍체를 반환 받습니다.
        model.addAttribute("items",items); //조회한 상품 데이터 및 페이징 정보를 뷰에 전달합니다.
        model.addAttribute("itemSearchDto",itemSearchDto); //페이지 전환 시 기존 검색 유지한 채 이동 할 수 있도록 뷰에 다시 전달합니다.
        model.addAttribute("maxPage",5); //상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수입니다. 5로 설정했으므로 최대 5개의 이동할 페이지 번호를 보여줍니다.
        return "item/itemMng";
    }

    //상품 상세 페이지로 이동할 수 있도록 코드 추가
    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDetail(itemId); //기존에 상품 수정 페이지 구현애서 미리 만들어둔 상품을 가지고 오는 로직을 똑같이 사용
        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }


}