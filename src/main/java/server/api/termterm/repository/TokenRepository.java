package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.jwt.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
