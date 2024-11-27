package project.shoppingmall.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.shoppingmall.dto.MemberFormDto;
import project.shoppingmall.entity.Member;
import project.shoppingmall.service.MemberService;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping("/new")
    public String memberForm(MemberFormDto memberFormDto) {
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";
        //return "redirect:/";는 클라이언트가 요청한 URL에 대한 처리가 끝난 후,
        // 클라이언트를 루트 URL로 리다이렉트(해당 URL로 이동)하라는 지시를 하는 것입니다.
        // 예를 들어, 사용자가 어떤 작업을 완료한 후 홈 페이지로 돌아가고 싶을 때 이 코드를 사용할 수 있습니다.

    }

    //회원가입이 성공하면 메인페이지로 리다이렉트 시켜주고,
    //화면 정보 검증 및 중복회원 가입 조건에 의해 실패한다면 다시 회원 가입 페이지로 돌아가 실패 이유를 화면에 출력해주겠습니다.
    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) { //검증할려는 객체의 앞에 @Valid 어노테이션 선언, 검사 후 결과는 bindingResult에 담아줍니다.
        if (bindingResult.hasErrors()) {
            return "member/memberForm"; //실패해서 오류 뜨면 회원 가입 페이지로 돌아가기
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); //회원 가입 페이지로 돌아가 실패 이유를 화면에 출력
            return "member/memberForm";
        }

        return "redirect:/"; //회원가입이 성공하면 메인페이지로 리다이렉트 시켜줌
    }

    @GetMapping("/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

}
