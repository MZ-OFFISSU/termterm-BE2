package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.category.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c " +
            "where c.name IN :names")
    List<Category> findByName(@Param("names") List<String> names);
}
