package name.marcocirillo.library.util;

import org.junit.jupiter.api.Test;

import static name.marcocirillo.library.util.Strings.escapeSql;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringsTest {

    @Test
    void test_sqlEscape() {
        assertEquals("Marco''s Friends'' Books", escapeSql("Marco's Friends' Books"));
    }
}