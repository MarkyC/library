package name.marcocirillo.library.cron;

import name.marcocirillo.library.checkout.overdue.OverdueCheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OverdueCheckoutCronJob {
    private static final Logger LOG = LoggerFactory.getLogger(OverdueCheckoutCronJob.class.getName());

    private final OverdueCheckoutService service;

    @Autowired
    public OverdueCheckoutCronJob(OverdueCheckoutService service) {
        this.service = service;
    }

    @Scheduled(cron = "0 0 5 * * *") // every day at 5am UTC
    public void notifyOverdueCheckouts() {
        LOG.info("Sending overdue Checkout Notifications");
        try {
            service.notifyOverdueCheckouts();
        } catch (Exception e) {
            LOG.error("Failed to send overdue Checkout Notifications", e);
        }
    }
}
