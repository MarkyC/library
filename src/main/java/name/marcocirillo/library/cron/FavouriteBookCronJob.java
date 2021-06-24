package name.marcocirillo.library.cron;

import name.marcocirillo.library.book.favourite.FavouriteBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FavouriteBookCronJob {
    private static final Logger LOG = LoggerFactory.getLogger(FavouriteBookCronJob.class.getName());

    private final FavouriteBookService service;

    @Autowired
    public FavouriteBookCronJob(FavouriteBookService service) {
        this.service = service;
    }

    @Scheduled(cron = "0 0 6 * * MON") // every Monday at 6am UTC
    public void notifyFavouriteBooks() {
        LOG.info("Sending favourite Book Notifications");
        try {
            service.notifyFavouriteBooks();
        } catch (Exception e) {
            LOG.error("Failed to send of favourite Book Notifications", e);
        }
    }
}
