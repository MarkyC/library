package name.marcocirillo.library.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingNotificationService implements NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingNotificationService.class.getName());
    @Override
    public void sendNotification(Notification notification) {
        LOG.info(String.format("Sending Notification%nTo: %s%nSubject: %s%nBody: %s",
                notification.getTo(), notification.getSubject(), notification.getText()));
    }
}
