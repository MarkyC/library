package name.marcocirillo.library.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, UUID> {
    List<Checkout> getAllByAccountIdAndReturned(UUID accountId, boolean returned);

    List<Checkout> getAllByDueBeforeAndReturned(OffsetDateTime due, boolean returned);

    /**
     * @return  The total amount of Books this Account has checked out
     */
    default int getUnreturnedBooksCheckedOutByAccount(UUID accountId) {
        return getAllByAccountIdAndReturned(accountId, false).size();
    }

    /**
     * @return Checkouts that haven't been returned whose due date is before the specified date
     */
    default List<Checkout> getOverdueCheckouts(OffsetDateTime due) {
        return getAllByDueBeforeAndReturned(due, false);
    }
}
