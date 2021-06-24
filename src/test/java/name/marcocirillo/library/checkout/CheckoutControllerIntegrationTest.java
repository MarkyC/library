package name.marcocirillo.library.checkout;

import name.marcocirillo.library.author.Author;
import name.marcocirillo.library.author.TestAuthorRepository;
import name.marcocirillo.library.base.BaseIntegrationTest;
import name.marcocirillo.library.book.Book;
import name.marcocirillo.library.book.TestBookRepository;
import name.marcocirillo.library.checkout.model.ImmutableBookCheckoutRequest;
import name.marcocirillo.library.checkout.model.ImmutableCheckoutApiRequest;
import name.marcocirillo.library.util.TestAuthUtils;
import name.marcocirillo.library.util.TestResourceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static name.marcocirillo.library.checkout.model.BookInventoryErrorApiResponse.Reason.NOT_AVAILABLE;
import static name.marcocirillo.library.checkout.model.BookInventoryErrorApiResponse.Reason.NOT_ENOUGH_STOCK;
import static name.marcocirillo.library.checkout.model.BookInventoryErrorApiResponse.Reason.OUT_OF_STOCK;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CheckoutControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestResourceUtils testResourceUtils;

    @Autowired
    private TestAuthorRepository testAuthorRepository;

    @Autowired
    private TestAuthUtils testAuthUtils;

    @Autowired
    private TestBookRepository testBookRepository;

    @Test
    void checkout() throws Exception {
        String jwt = testAuthUtils.createAccountAndJwt().getRight();

        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author);

        mockMvc.perform(post("/checkout")
                .content(testResourceUtils.objectToString(ImmutableCheckoutApiRequest.builder()
                        .books(List.of(ImmutableBookCheckoutRequest.builder()
                                .id(book.getId())
                                .quantity(1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkouts").value(hasSize(1)))
                .andExpect(jsonPath("$.checkouts[0].id").isNotEmpty())
                .andExpect(jsonPath("$.checkouts[0].due").isNotEmpty())
                .andExpect(jsonPath("$.checkouts[0].bookId").value(book.getId().toString()));
    }

    @Test
    void checkout_notAvailable() throws Exception {
        String jwt = testAuthUtils.createAccountAndJwt().getRight();

        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author, false);

        mockMvc.perform(post("/checkout")
                .content(testResourceUtils.objectToString(ImmutableCheckoutApiRequest.builder()
                        .books(List.of(ImmutableBookCheckoutRequest.builder()
                                .id(book.getId())
                                .quantity(1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].bookId").value(book.getId().toString()))
                .andExpect(jsonPath("$.errors[0].inventoryLevel").value(0))
                .andExpect(jsonPath("$.errors[0].reason").value(NOT_AVAILABLE.name()));
    }

    @Test
    void checkout_outOfStock() throws Exception {
        String jwt = testAuthUtils.createAccountAndJwt().getRight();

        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author, true, 0);

        mockMvc.perform(post("/checkout")
                .content(testResourceUtils.objectToString(ImmutableCheckoutApiRequest.builder()
                        .books(List.of(ImmutableBookCheckoutRequest.builder()
                                .id(book.getId())
                                .quantity(1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].bookId").value(book.getId().toString()))
                .andExpect(jsonPath("$.errors[0].inventoryLevel").value(0))
                .andExpect(jsonPath("$.errors[0].reason").value(OUT_OF_STOCK.name()));
    }

    @Test
    void checkout_notEnoughStock() throws Exception {
        String jwt = testAuthUtils.createAccountAndJwt().getRight();

        int stock = 1;
        Author author = testAuthorRepository.createAuthor();
        Book book = testBookRepository.createBook(author, true, stock);

        mockMvc.perform(post("/checkout")
                .content(testResourceUtils.objectToString(ImmutableCheckoutApiRequest.builder()
                        .books(List.of(ImmutableBookCheckoutRequest.builder()
                                .id(book.getId())
                                .quantity(stock + 1) // try to Checkout 1 more than is in stock
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(hasSize(1)))
                .andExpect(jsonPath("$.errors[0].bookId").value(book.getId().toString()))
                .andExpect(jsonPath("$.errors[0].inventoryLevel").value(stock))
                .andExpect(jsonPath("$.errors[0].reason").value(NOT_ENOUGH_STOCK.name()));
    }
}