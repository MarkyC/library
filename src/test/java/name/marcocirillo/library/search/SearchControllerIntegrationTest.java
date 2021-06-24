package name.marcocirillo.library.search;

import name.marcocirillo.library.author.Author;
import name.marcocirillo.library.author.TestAuthorRepository;
import name.marcocirillo.library.base.BaseIntegrationTest;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.TestBookRepository;
import name.marcocirillo.library.book.category.TestBookCategoryRepository;
import name.marcocirillo.library.category.Category;
import name.marcocirillo.library.category.TestCategoryRepository;
import name.marcocirillo.library.seed.search.SearchIndexService;
import name.marcocirillo.library.seed.search.SearchSeedService;
import name.marcocirillo.library.seed.search.db.SearchBook;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static name.marcocirillo.library.seed.search.SearchIndexService.Index.BOOK_TEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SearchIndexService searchIndexService;

    @Autowired
    @Qualifier("bookIndex")
    private SearchIndexService.Index bookIndex;

    @Autowired
    private SearchSeedService searchSeedService;

    @Value("classpath:search/book.schema.json")
    private Resource bookSchema;

    @Autowired
    private TestAuthorRepository testAuthorRepository;

    @Autowired
    private TestBookRepository testBookRepository;

    @Autowired
    private TestBookCategoryRepository testBookCategoryRepository;

    @Autowired
    private TestCategoryRepository testCategoryRepository;

    @BeforeEach
    void setUp() throws IOException {
        searchIndexService.deleteIndex(BOOK_TEST);
        searchIndexService.createIndexIfNotPresent(BOOK_TEST, Files.readString(bookSchema.getFile().toPath()));
    }

    @Test
    void search_exactMatch() throws Exception {
        Pair<Author, Book> authorBookPair = createAuthorAndBookAndSeed();
        Author author = authorBookPair.getLeft();
        Book book = authorBookPair.getRight();

        mockMvc.perform(get("/search")
                .param("title", book.getTitle())
                .param("authorName", author.getName())
                .param("isbn", book.getIsbn()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    @Test
    void search_partialTitle() throws Exception {
        Pair<Author, Book> authorBookPair = createAuthorAndBookAndSeed();
        Author author = authorBookPair.getLeft();
        Book book = authorBookPair.getRight();

        // missing the last 3 characters in the title
        String partialTitle = book.getTitle().substring(0, book.getTitle().length() - 3);

        mockMvc.perform(get("/search")
                .param("title", partialTitle))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    @Test
    void search_partialAuthor() throws Exception {
        Pair<Author, Book> authorBookPair = createAuthorAndBookAndSeed();
        Author author = authorBookPair.getLeft();
        Book book = authorBookPair.getRight();

        // missing the last 3 characters in the title
        String partialAuthor = author.getName().substring(0, author.getName().length() - 3);
        mockMvc.perform(get("/search")
                .param("authorName", partialAuthor))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    @Test
    void search_isbn() throws Exception {
        Pair<Author, Book> authorBookPair = createAuthorAndBookAndSeed();
        Author author = authorBookPair.getLeft();
        Book book = authorBookPair.getRight();

        mockMvc.perform(get("/search")
                .param("isbn", book.getIsbn()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    @Test
    void search_authorId() throws Exception {
        Pair<Author, Book> authorBookPair = createAuthorAndBookAndSeed();
        Author author = authorBookPair.getLeft();
        Book book = authorBookPair.getRight();

        mockMvc.perform(get("/search")
                .param("authorId", author.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    @Test
    void search_categoryId() throws Exception {
        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author);
        Category category = testCategoryRepository.createCategory("Some Category");
        testBookCategoryRepository.createBookCategory(book, category);

        searchSeedService.seedBookSearch(bookIndex, Stream.of(new SearchBook(
                author.getId(),
                author.getName(),
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                List.of(category.getId()),
                book.getAvailable(),
                book.getStock()
        )));

        searchIndexService.refreshIndex(bookIndex);

        mockMvc.perform(get("/search")
                .param("categoryId", category.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isNotEmpty())
                .andExpect(jsonPath("$.results[0].authorName").value(author.getName()))
                .andExpect(jsonPath("$.results[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$.results[0].isbn").value(book.getIsbn()));
    }

    private Pair<Author, Book> createAuthorAndBookAndSeed() {
        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author);

        searchSeedService.seedBookSearch(bookIndex, Stream.of(new SearchBook(
                author.getId(),
                author.getName(),
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAvailable(),
                book.getStock()
        )));

        // force a refresh so the documents are immediately available for search
        searchIndexService.refreshIndex(bookIndex);

        return Pair.of(author, book);
    }
}