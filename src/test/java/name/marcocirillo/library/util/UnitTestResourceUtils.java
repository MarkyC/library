package name.marcocirillo.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

/**
 * For handling Resources in unit test (ie: not @SpringBootTest)
 */
public final class UnitTestResourceUtils {

    public String loadTemplateFile(String filePath) {
        return loadResourceFile(String.format("templates/%s", filePath));
    }

    public String loadResourceFile(String filePath) {
        try {
            InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filePath));
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
