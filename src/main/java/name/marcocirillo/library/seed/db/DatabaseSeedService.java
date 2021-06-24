package name.marcocirillo.library.seed.db;

import java.nio.file.Path;
import java.sql.SQLException;

public interface DatabaseSeedService {
    /**
     * Seed the DB with the given SQL file.
     * This will execute all statements in the file, the input here should NOT be user-generated!
     * @param sqlFile   The SQL file to seed the database with
     * @throws SQLException If there are any errors in executing the SQL or connection issues
     *                      (unfortunately SQLException is a checked exception so it must live here)
     */
    void seed(Path sqlFile) throws SQLException;
}
