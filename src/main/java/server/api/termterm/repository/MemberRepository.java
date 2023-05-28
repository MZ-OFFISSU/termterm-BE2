package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.member.Member;


import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndEmail(String socialId, String email);
    Optional<Member> findByIdentifier(String identifier);

//    @Query(value = "select new server.api.termterm.dto.member.MemberInfoDto(" +
//            "m.id, m.account)" +
//            "from Member m " +
//            "where m.account = :account")
//    Optional<MemberInfoDto> getMemberInfoDtoByAccount(@Param("account") String account);
    Boolean existsBySocialIdAndEmail(String socialId, String email);
    Boolean existsByNicknameIgnoreCase(String nickname);

}