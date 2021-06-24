package name.marcocirillo.library.util;

import java.util.stream.Stream;

public final class Objects {
    /**
     * Returns the first non-null object
     * @param objects   objects to search through
     * @param <T>       type of objects being passed in/returned
     * @throws NullPointerException if there are no non-null objects passed in
     * @return the first non-null object passed in
     */
    @SafeVarargs
    public static <T> T firstNonNull(T... objects) {
        return Stream.of(objects)
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }
}
