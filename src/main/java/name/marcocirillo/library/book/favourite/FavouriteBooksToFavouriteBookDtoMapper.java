package name.marcocirillo.library.book.favourite;

import name.marcocirillo.library.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FavouriteBooksToFavouriteBookDtoMapper implements Function<Collection<FavouriteBook>, FavouriteBookDto> {
    private final BookRepository bookRepository;

    @Autowired
    public FavouriteBooksToFavouriteBookDtoMapper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public FavouriteBookDto apply(Collection<FavouriteBook> favouriteBooks) {
        return ImmutableFavouriteBookDto.builder()
                .books(favouriteBooks.stream()
                        .map(book -> bookRepository.getById(book.getId()))
                        .map(book -> ImmutableBook.builder()
                                .title(book.getTitle())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
