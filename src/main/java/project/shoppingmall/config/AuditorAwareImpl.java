package project.shoppingmall.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//자동으로 생성자와 수정자 정보를 기록하기 위해 사용되는 인터페이스 AuditorAware를 구현한 클래스입니다.
public class AuditorAwareImpl implements AuditorAware<String> {
    //현재 인증된 사용자 정보를 가져오는 로직
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Authentication 객체는 현재 인증된 사용자의 정보를 포함
        String userId = ""; //만약 사용자가 인증되지 않았거나 익명 사용자라면 기본값(여기서는 빈 문자열 "")이 사용됩니다.
        if(authentication != null) { //authentication이 null이 아닌 경우(즉, 사용자가 로그인된 상태인 경우
            userId = authentication.getName(); //현재 로그인 한 사용자의 이름을 가져와서 등록자(@CreatedBy) 또는 수정자(@LastModifiedBy)로 지정
        }
        return Optional.of(userId); //반환값은 Optional<String> 타입으로, 현재 로그인된 사용자의 ID를 Optional로 감싸서 반환합니다.
    }
}
