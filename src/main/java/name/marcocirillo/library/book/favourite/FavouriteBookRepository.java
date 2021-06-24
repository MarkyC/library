package name.marcocirillo.library.book.favourite;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;

@Repository
public interface FavouriteBookRepository extends JpaRepository<FavouriteBook, UUID> {
    @QueryHints(value = {
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    List<FavouriteBook> getAllByAccountIdOrderByTimesCheckedOutDesc(UUID accountId, Pageable page);

    @QueryHints(value = {
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("SELECT DISTINCT fb.accountId FROM FavouriteBook fb")
    Stream<UUID> getAllAccountIds();

    int MAX_FAVOURITE_BOOKS = 5;
    default Collection<FavouriteBook> getFavouriteBooksForAccount(UUID accountId) {
        return getAllByAccountIdOrderByTimesCheckedOutDesc(accountId, Pageable.ofSize(MAX_FAVOURITE_BOOKS));
    }
}
