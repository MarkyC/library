package name.marcocirillo.library.util;

public final class Numbers {
    /**
     * Converts a long to an (unsigned) int.
     * Negative numbers become 0 and numbers larger than int can hold become Integer.MAX_VALUE
     *
     * @param value the long to convert to an int
     */
    public static int longToUnsignedIntClamped(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (value < 0) {
            return 0;
        } else {
            return (int) value;
        }
    }
}
