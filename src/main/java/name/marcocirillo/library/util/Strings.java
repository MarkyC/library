package name.marcocirillo.library.util;

public final class Strings {
    /**
     * Escape single quotes in SQL statements
     * @param input The SQL statement
     * @return  The input SQL statement, with single quotes escaped: Marco's becomes Marco''s
     */
    public static String escapeSql(String input) {
        return input.replaceAll("'", "''");
    }
}
