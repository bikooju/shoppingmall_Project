package project.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //JPA의 Auditing 기능을 활성화 (이렇게 해도 되고 Application에 @EnableJpaAuditing 붙여도 됌)
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() { //등록자와 수정자를 조회하는 AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
