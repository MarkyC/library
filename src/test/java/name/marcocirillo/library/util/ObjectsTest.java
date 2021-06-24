package name.marcocirillo.library.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsTest {

    @Test
    void firstNonNull() {
        Object expected = new Object();
        assertEquals(expected, Objects.firstNonNull(null, expected));
    }

    @Test
    void firstNonNull_allNull() {
        assertThrows(NullPointerException.class, () -> Objects.firstNonNull(null, null, null));
    }
}