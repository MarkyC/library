package name.marcocirillo.library.notification.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import name.marcocirillo.library.notification.template.TemplateRenderException;
import name.marcocirillo.library.notification.template.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static name.marcocirillo.library.notification.template.TemplateRenderException.ErrorCodes.UNKNOWN;

@Service
public class FreeMarkerTemplateService implements TemplateService {
    private final Configuration freeMarkerConfig;

    @Autowired
    public FreeMarkerTemplateService(Configuration freeMarkerConfig) {
        this.freeMarkerConfig = freeMarkerConfig;
    }

    public String render(String templateFile, Map<String, ?> data) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    freeMarkerConfig.getTemplate(templateFile, StandardCharsets.UTF_8.toString()),
                    data
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (TemplateException e) {
            throw new TemplateRenderException(UNKNOWN, e);
        }
    }
}