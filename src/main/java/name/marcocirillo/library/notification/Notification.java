package name.marcocirillo.library.notification;

import org.immutables.value.Value;

import java.util.Collections;
import java.util.List;

@Value.Immutable
public interface Notification {
    default String getFrom() {
        return "noreply@library";
    }
    List<String> getTo();
    default List<String> getCc() {
        return Collections.emptyList();
    }
    default List<String> getBcc() {
        return Collections.emptyList();
    }
    String getSubject();
    String getText();
    String getHtml();
}
