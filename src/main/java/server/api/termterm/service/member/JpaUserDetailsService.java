package server.api.termterm.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import server.api.termterm.domain.member.CustomUserDetails;
import server.api.termterm.domain.member.Member;
import server.api.termterm.repository.MemberRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.member.MemberResponseType;


@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByIdentifier(username)
                .orElseThrow(() -> new BizException(MemberResponseType.NOT_FOUND_USER));

        return new CustomUserDetails(member);
    }
}
