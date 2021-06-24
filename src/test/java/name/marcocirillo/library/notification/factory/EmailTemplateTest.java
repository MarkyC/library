package name.marcocirillo.library.notification.factory;

import name.marcocirillo.library.util.UnitTestResourceUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplateTest {
    private final UnitTestResourceUtils unitTestResourceUtils = new UnitTestResourceUtils();

    @Test
    void allEmailTemplatesExist() {
        for (EmailTemplate template : EmailTemplate.values()) {
            String name = template.name();
            try {
                assertFalse(unitTestResourceUtils.loadTemplateFile(template.getHtmlTemplate()).isEmpty(),
                        String.format("HTML template '%s' is not empty", name));
            } catch (NullPointerException npe) {
                // we could just let this throw, but then you'd be scratching your head as to which template failed
                fail(String.format("HTML template '%s' doesn't exist", name));
            }

            try {
                assertFalse(unitTestResourceUtils.loadTemplateFile(template.getHtmlTemplate()).isEmpty(),
                        String.format("Text template '%s' is not empty", name));
            } catch (NullPointerException npe) {
                fail(String.format("Text template '%s' doesn't exist", name));
            }
        }
    }
}