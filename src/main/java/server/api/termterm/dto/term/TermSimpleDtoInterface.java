package server.api.termterm.dto.term;

import java.sql.Clob;

public interface TermSimpleDtoInterface {
    Long getTermId();
    String getName();
    Clob getDescription();
    String getBookmarked();
}
