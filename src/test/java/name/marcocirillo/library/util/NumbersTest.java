package name.marcocirillo.library.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumbersTest {

    @Test
    void longToUnsignedIntClamped() {
        assertEquals(Integer.MAX_VALUE, Numbers.longToUnsignedIntClamped(Integer.MAX_VALUE));
        assertEquals(0, Numbers.longToUnsignedIntClamped(Integer.MAX_VALUE + 1));
        assertEquals(Integer.MAX_VALUE, Numbers.longToUnsignedIntClamped((long) Integer.MAX_VALUE + 1));
        assertEquals(3, Numbers.longToUnsignedIntClamped(3));
        assertEquals(0, Numbers.longToUnsignedIntClamped(-3));
    }
}