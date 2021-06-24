package name.marcocirillo.library.book.category;

import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.category.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestBookCategoryRepository {
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    public BookCategory createBookCategory(Book book, Category category) {
        return bookCategoryRepository.save(new BookCategory(UUID.randomUUID(), book.getId(), category.getId()));
    }

}