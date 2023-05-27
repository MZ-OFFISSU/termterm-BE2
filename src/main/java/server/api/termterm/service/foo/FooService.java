package server.api.termterm.service.foo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.foo.Foo;
import server.api.termterm.dto.foo.FooDto;
import server.api.termterm.repository.FooRepository;
import server.api.termterm.response.BizException;
import server.api.termterm.response.foo.FooResponseType;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FooService {
    private final FooRepository fooRepository;

    public FooDto getFoo(String name, String title){
        // service 에서도 다음과 같이 exception 을 던지면 알아서 response message를 만들어 클라이언트에게 응답해 줍니다.
        if (title.equals("error")){
            log.error(FooResponseType.INVALID_PARAMETER.getMessage());
            throw new BizException(FooResponseType.INVALID_PARAMETER);
        }

        // Invoke Exception in Optional Class
        Foo foo = fooRepository.findFoo(name, title)
                .orElseThrow(() -> new BizException(FooResponseType.INVALID_PARAMETER));

        return FooDto.builder()
                .name(foo.getName())
                .title(foo.getTitle())
                .build();
    }
}
