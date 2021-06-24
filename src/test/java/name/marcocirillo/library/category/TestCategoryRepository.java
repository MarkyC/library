package name.marcocirillo.library.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestCategoryRepository {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(String name) {
        return categoryRepository.save(new Category(UUID.randomUUID(), name));
    }
}