package name.marcocirillo.library.notification.template;

import java.util.Map;

public interface TemplateService {
    String render(String templateFile, Map<String, ?> data);
}