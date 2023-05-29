package server.api.termterm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.category.Category;
import server.api.termterm.repository.CategoryRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.category.CategoryResponseType;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category findByName(String name){
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new BizException(CategoryResponseType.CATEGORY_NOT_EXISTS));

    }
}
