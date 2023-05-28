package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.term.Term;

public interface TermRepository extends JpaRepository<Term, Long> {


}
